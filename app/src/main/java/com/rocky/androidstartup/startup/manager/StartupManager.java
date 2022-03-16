package com.rocky.androidstartup.startup.manager;

import android.content.Context;
import android.os.Looper;

import com.rocky.androidstartup.startup.StartupSortStore;
import com.rocky.androidstartup.startup.run.StartupRunnable;
import com.rocky.androidstartup.startup.sort.TopologySort;
import com.rocky.androidstartup.task.Startup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/15
 *     des    ：这是 Startup 的管理类
 *              因为任务可以分批次执行 所以不能使用全局单例
 * </pre>
 */
public class StartupManager {

    private final Context context;
    private final List<Startup<?>> tasks;
    private StartupSortStore startupSortStore;
    //这是整个任务链的闭锁
    private CountDownLatch awaitCountDownLatch;

    public StartupManager(Context context, List<Startup<?>> tasks, CountDownLatch awaitCountDownLatch) {
        this.context = context;
        this.tasks = tasks;
        this.awaitCountDownLatch = awaitCountDownLatch;
    }

    public StartupManager start() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("请在主线程调用！");
        }

        //对任务进行排序
        startupSortStore = TopologySort.sort(tasks);
        List<Startup<?>> sortedTasks = startupSortStore.getResult();
        for (Startup<?> sortedTask : sortedTasks) {
            //把任务扔进runnable中去执行
            StartupRunnable startupRunnable = new StartupRunnable(context, sortedTask, this);
            //这个思想来源与okhttp中的任务队列
            // 在okhttp发送请求之后  会进入相应的队列  同步和异步队列
            //在这里 同步相当主线程 直接执行
            //异步相当于字想成  放入到线程池执行
            if (sortedTask.callCreateOnMainThread()) {
                startupRunnable.run();
            } else {
                sortedTask.executor().execute(startupRunnable);
            }
        }
        return this;
    }

    public void notifyChildren(Startup<?> startup) {
        //通知任务去执行
        if (!startup.callCreateOnMainThread() &&
                startup.waitOnMainThread()) {
            awaitCountDownLatch.countDown();
        }

        //拿到任务的后续任务map
        Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap = startupSortStore.getStartupChildrenMap();
        //判断startup 这个任务是否有后续任务
        if (startupChildrenMap.containsKey(startup.getClass())) {
            List<Class<? extends Startup>> classList = startupChildrenMap.get(startup.getClass());
            for (Class<? extends Startup> aClass : classList) {
                //遍历拿到后续任务 去通知执行
                Startup<?> task = startupSortStore.getStartupMap().get(aClass);
                task.toNotify();

            }
        }


    }

    public void await() {
        try {
            //是主线程等待
            awaitCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //基于分阶段处理任务，不能用单例
    public static class Builder {
        private List<Startup<?>> startupList = new ArrayList<>();

        public Builder addStartup(Startup<?> startup) {
            startupList.add(startup);
            return this;
        }

        public Builder addAllStartup(List<Startup<?>> startups) {
            startupList.addAll(startups);
            return this;
        }

        public StartupManager build(Context context) {
            //遍历所有任务 查看需要在子线程执行的且需要主线程等待的个数
            AtomicInteger needAwaitCount = new AtomicInteger();
            for (Startup<?> startup : startupList) {
                if (!startup.callCreateOnMainThread() && startup.waitOnMainThread()) {
                    needAwaitCount.incrementAndGet();
                }
            }
            //根据任务数新建一个闭锁
            CountDownLatch awaitCountDownLatch = new CountDownLatch(needAwaitCount.get());
            return new StartupManager(context, startupList, awaitCountDownLatch);
        }
    }


}

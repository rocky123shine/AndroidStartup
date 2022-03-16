package com.rocky.androidstartup.task.impl;


import com.rocky.androidstartup.startup.manager.ExecutorManager;
import com.rocky.androidstartup.task.Startup;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import android.os.Process;


/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/14
 *     des    : 默认是实现的任务的抽象类
 * </pre>
 */
public abstract class AndroidStartup<T> implements Startup<T> {
    //入度数初始化
    /**
     * 闭锁
     * 阻塞任务执行 和 唤醒任务执行
     */
    private CountDownLatch mWaitCountDown = new CountDownLatch(getDependenciesCount());


    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return null;
    }

    @Override
    public int getDependenciesCount() {
        List<Class<? extends Startup<?>>> dependencies = dependencies();
        return dependencies == null ? 0 : dependencies.size();
    }

    /************************顶层接口的扩展*************************/
    /**
     * 默认子线程执行
     **/
    @Override
    public boolean callCreateOnMainThread() {
        return false;
    }

    @Override
    public Executor executor() {
        // 使用线程池管理类 选择线程池
        //默认使用io
        return ExecutorManager.ioExecutor;
    }

    /**
     * 设置优先级
     * 值越小 优先级越高
     */
    @Override
    public int getThreadPriority() {
        return Process.THREAD_PRIORITY_DEFAULT;
    }

    @Override
    public void toWait() {
        try {
            mWaitCountDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toNotify() {
        //通知数量减一  为0 怎不会在等待
        mWaitCountDown.countDown();

    }

    /**
     * 默认不需要主线程等待自己执行完成
     */
    @Override
    public boolean waitOnMainThread() {
        return false;
    }
}

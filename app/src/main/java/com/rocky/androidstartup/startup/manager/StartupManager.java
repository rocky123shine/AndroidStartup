package com.rocky.androidstartup.startup.manager;

import android.content.Context;

import com.rocky.androidstartup.startup.Result;
import com.rocky.androidstartup.startup.StartupSortStore;
import com.rocky.androidstartup.startup.sort.TopologySort;
import com.rocky.androidstartup.task.Startup;

import java.util.ArrayList;
import java.util.List;

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

    public StartupManager(Context context, List<Startup<?>> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public StartupManager start() {
        //对任务进行排序
        startupSortStore = TopologySort.sort(tasks);
        List<Startup<?>> sortedTasks = startupSortStore.getResult();
        for (Startup<?> sortedTask : sortedTasks) {
            //执行任务
            Object o = sortedTask.create(context);
            //缓存结果
            StartupCacheManager.getInstance().saveInitializedComponent(sortedTask.getClass(), new Result(o));
        }
        return this;
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
            return new StartupManager(context, startupList);
        }
    }


}

package com.rocky.androidstartup.startup.run;

import android.content.Context;
import android.os.Process;

import com.rocky.androidstartup.startup.Result;
import com.rocky.androidstartup.startup.manager.StartupCacheManager;
import com.rocky.androidstartup.startup.manager.StartupManager;
import com.rocky.androidstartup.task.Startup;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/16
 * </pre>
 */
public class StartupRunnable implements Runnable {

    private StartupManager startupManager;
    private Startup<?> startup;
    private Context context;

    public StartupRunnable(Context context, Startup<?> startup,
                           StartupManager startupManager) {
        this.context = context;
        this.startup = startup;
        this.startupManager = startupManager;
    }

    @Override
    public void run() {
        //在这里执行任务
        //设置线程的优先级
        Process.setThreadPriority(startup.getThreadPriority());
        startup.toWait();
        Object result = startup.create(context);
        StartupCacheManager.getInstance().saveInitializedComponent(startup.getClass(),
                new Result(result));
        //当前任务执行完成后，调用后序任务的toNotify()
        startupManager.notifyChildren(startup);
    }
}

package com.rocky.androidstartup.startup.manager;

import com.rocky.androidstartup.startup.Result;
import com.rocky.androidstartup.task.Startup;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/15
 *     des    : 这是任务执行结果缓存管理工具
 *              结果全局只需一份  所以使用单例
 * </pre>
 */
public class StartupCacheManager {
    private volatile static StartupCacheManager mInstance;
    //每个任务的执行结果 缓存到一个表中
    //考虑到安全性  在此使用ConcurrentHashMap
    ConcurrentHashMap<Class<? extends Startup>, Result> mInitializedComponents = new ConcurrentHashMap();

    private StartupCacheManager() {
    }

    public static StartupCacheManager getInstance() {
        if (mInstance == null) {
            synchronized (StartupCacheManager.class) {
                if (mInstance == null) {
                    mInstance = new StartupCacheManager();
                }
            }
        }
        return mInstance;
    }



    /**
     * save result of initialized component.
     */
    public void saveInitializedComponent(Class<? extends Startup> zClass, Result result) {
        mInitializedComponents.put(zClass, result);
    }

    /**
     * check initialized.
     */
    public boolean hadInitialized(Class<? extends Startup> zClass) {
        return mInitializedComponents.containsKey(zClass);
    }

    public <T> Result<T> obtainInitializedResult(Class<? extends Startup<T>> zClass) {
        return mInitializedComponents.get(zClass);
    }


    public void remove(Class<? extends Startup> zClass) {
        mInitializedComponents.remove(zClass);
    }

    public void clear() {
        mInitializedComponents.clear();
    }

}

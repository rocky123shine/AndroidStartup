package com.rocky.androidstartup.task;

import java.util.concurrent.Executor;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/15
 *     des    ：采用顶层接口给框架扩展
 * </pre>
 */
public interface Dispatcher {
    /**
     * 返回是否在主线程执行
     */
    boolean callCreateOnMainThread();

    /**
     * 让每个任务都可以指定自己执行在哪个线程沲
     */
    Executor executor();

    /**
     * 指定线程的优先级
     */
    int getThreadPriority();


    /**
     * 等待
     */
    void toWait();

    /**
     * 有父任务执行完毕
     * 计数器-1
     */
    void toNotify();

    /**
     * 是否需要主线程等待该任务执行完成
     * callCreateOnMainThread()方法返回false才有意义
     */
    boolean waitOnMainThread();

}

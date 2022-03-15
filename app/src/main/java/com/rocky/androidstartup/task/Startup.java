package com.rocky.androidstartup.task;

import android.content.Context;

import java.util.List;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/14
 *     des    : 这个是所有任务的顶层接口 规范了任务
 * </pre>
 */
public interface Startup<T> {
    //使用者创建一个任务
    T create(Context context);

    // 任务的依赖任务
    List<Class<? extends Startup<?>>> dependencies();

    /**
     * 入度数
     * 即是本任务依赖的任务的数量
     * 执行本任务之前 为保证本任务执行 所需完成的任务的数量
     */
    int getDependenciesCount();

}

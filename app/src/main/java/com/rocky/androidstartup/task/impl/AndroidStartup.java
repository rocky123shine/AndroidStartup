package com.rocky.androidstartup.task.impl;

import android.content.Context;

import com.rocky.androidstartup.task.Startup;

import java.util.List;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/14
 *     des    : 默认是实现的任务的抽象类
 * </pre>
 */
public abstract class AndroidStartup<T> implements Startup<T> {


    @Override
    public List<Class<? extends Startup<?>>> dependencies() {

        return null;
    }

    @Override
    public int getDependenciesCount() {
        List<Class<? extends Startup<?>>> dependencies = dependencies();
        return dependencies == null ? 0 : dependencies.size();
    }



}

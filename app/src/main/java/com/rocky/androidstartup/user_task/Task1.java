package com.rocky.androidstartup.user_task;

import android.content.Context;

import android.os.SystemClock;

import androidx.annotation.Nullable;


import com.rocky.androidstartup.task.Startup;
import com.rocky.androidstartup.task.impl.AndroidStartup;

import java.util.List;

public class Task1 extends AndroidStartup<Void> {

    @Nullable
    @Override
    public Void create(Context context) {
        LogUtils.log(" Task1：学习Java基础");
        SystemClock.sleep(3_000);
        LogUtils.log(" Task1：掌握Java基础");
        return null;
    }

    //执行此任务需要依赖哪些任务
    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return super.dependencies();
    }

}

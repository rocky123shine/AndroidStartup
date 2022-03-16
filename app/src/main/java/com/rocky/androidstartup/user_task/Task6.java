package com.rocky.androidstartup.user_task;

import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;

import com.rocky.androidstartup.task.Startup;
import com.rocky.androidstartup.task.impl.AndroidStartup;

import java.util.ArrayList;
import java.util.List;

public class Task6 extends AndroidStartup<Void> {


    @Override
    public Void create(Context context) {
        String t = Looper.myLooper() == Looper.getMainLooper()
                ? "主线程: " : "子线程: ";
        LogUtils.log(t + " Task6：done111111");
        SystemClock.sleep(500);
        LogUtils.log(t + " Task6：done222222");
        return null;
    }


    //执行此任务需要依赖哪些任务
    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return null;
    }

}

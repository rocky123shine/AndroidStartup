package com.rocky.androidstartup.user_task;

import android.content.Context;
import android.os.SystemClock;

import com.rocky.androidstartup.task.Startup;
import com.rocky.androidstartup.task.impl.AndroidStartup;

import java.util.ArrayList;
import java.util.List;

public class Task4 extends AndroidStartup<Void> {

    static List<Class<? extends Startup<?>>> depends;

    static {
        depends = new ArrayList<>();
        depends.add(Task2.class);
    }

    @Override
    public Void create(Context context) {
        LogUtils.log( " Task4：学习Http");
        SystemClock.sleep(1_000);
        LogUtils.log( " Task4：掌握Http");
        return null;
    }

    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return depends;
    }

}

package com.rocky.androidstartup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rocky.androidstartup.startup.StartupSortStore;
import com.rocky.androidstartup.startup.sort.TopologySort;
import com.rocky.androidstartup.task.Startup;
import com.rocky.androidstartup.user_task.Task1;
import com.rocky.androidstartup.user_task.Task2;
import com.rocky.androidstartup.user_task.Task3;
import com.rocky.androidstartup.user_task.Task4;
import com.rocky.androidstartup.user_task.Task5;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                testStep1();
            }
        }).start();

    }

    private void testStep1() {
        List list = new ArrayList<Startup>();

        list.add(new Task5());
        list.add(new Task4());
        list.add(new Task3());
        list.add(new Task2());
        list.add(new Task1());

        StartupSortStore sort = TopologySort.sort(list);
        for (Startup<?> startup : sort.getResult()) {
            startup.create(this);
        }
    }
}
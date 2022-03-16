package com.rocky.androidstartup.startup.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rocky.androidstartup.startup.manager.StartupManager;
import com.rocky.androidstartup.task.Startup;

import java.util.List;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/16
 *     des    : 自定义Provider 实现框架自动注册任务
 * </pre>
 */
public class StartupProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        //获取任务的有向无环图数据
        try {
            List<Startup<?>> startups = StartupInitializer.discoverAndInitial(getContext(), getClass().getName());
            new StartupManager.Builder()
                    .addAllStartup(startups)
                    .build(getContext())
                    .start()
                    .await();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

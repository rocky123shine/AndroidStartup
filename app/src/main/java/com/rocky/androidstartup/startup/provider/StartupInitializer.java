package com.rocky.androidstartup.startup.provider;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.text.TextUtils;

import com.rocky.androidstartup.task.Startup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/16
 *     des    ：获取初始化任务的信息工具类
 * </pre>
 */
public class StartupInitializer {
    public static String META_VALUE = "android.startup";

    //获取清单文件中注册的任务启动的provider 信息
    public static List<Startup<?>> discoverAndInitial(Context context,
                                                      String providerName) throws Exception {
        //用来保存有向无环图  任务的结构图
        Map<Class<? extends Startup>, Startup<?>> startups = new HashMap<>();
        //获取清单文件中的 provider 中的meta-data
        ComponentName provider = new ComponentName(context, providerName);
        ProviderInfo providerInfo = context.getPackageManager().getProviderInfo(provider, PackageManager.GET_META_DATA);
        Bundle metaData = providerInfo.metaData;
        for (String key : metaData.keySet()) {
            String value = metaData.getString(key);
            if (TextUtils.equals(META_VALUE, value)) {
                //反射注册的类
                Class<?> aClass = Class.forName(key);
                if (Startup.class.isAssignableFrom(aClass)) {//判断 aClass是否是Startup子类
                    doInitialize((Startup<?>) aClass.newInstance(), startups);
                }

            }

        }


        return new ArrayList<Startup<?>>(startups.values());
    }

    private static void doInitialize(Startup<?> startup, Map<Class<? extends Startup>, Startup<?>> startups) throws Exception {

        //避免重复 不能使用List
        startups.put(startup.getClass(), startup);

        if (startup.getDependenciesCount() != 0) {
            //遍历父任务
            for (Class<? extends Startup<?>> dependency : startup.dependencies()) {
                doInitialize(dependency.newInstance(), startups);
            }
        }
    }

}

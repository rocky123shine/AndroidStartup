package com.rocky.androidstartup.startup.sort;

import com.rocky.androidstartup.startup.StartupSortStore;
import com.rocky.androidstartup.task.Startup;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/14
 *     des    ：这个是对任务排序的工具类
 *              1. 保存原始任务数据
 *              2. 保存排序任务
 *
 * </pre>
 */
public class TopologySort {

    public static StartupSortStore sort(List<? extends Startup<?>> startupList) {
        //排序的时候 需要借助几张表

        //入度表
        Map<Class<? extends Startup>, Integer> inDegreeMap = new HashMap<>();
        //0度表
        Deque<Class<? extends Startup>> zeroDeque = new ArrayDeque<>();
        //保存原始数据
        Map<Class<? extends Startup>, Startup<?>> startupMap = new HashMap<>();
        //任务依赖表   所有依赖当前任务的集合   即当前任务执行之前 所有第一前序任务
        //或者说是某个任务的第一后继任务
        Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap = new HashMap<>();

        //1. 遍历数据 填入相应表
        for (Startup<?> startup : startupList) {
            //保存原始任务
            startupMap.put(startup.getClass(), startup);

            //存入入度表
            int dependenciesCount = startup.getDependenciesCount();
            inDegreeMap.put(startup.getClass(), dependenciesCount);

            //判断入度是否为0
            //入度为0 直接放入0度表
            if (dependenciesCount == 0) {
                zeroDeque.offer(startup.getClass());
            } else {
                //进入到这里 说明 有依赖  在这里处理依赖
                for (Class<? extends Startup<?>> dependency : startup.dependencies()) {
                    List<Class<? extends Startup>> children = startupChildrenMap.get(dependency);

                    if (null == children) {
                        children = new ArrayList<>();
                        startupChildrenMap.put(dependency, children);
                    }

                    children.add(startup.getClass());

                }

            }

        }

        //2. 删除图中入度为0的这些顶点,并更新全图，，最后完成排序
        List<Startup<?>> result = new ArrayList<>();
        //执行所有入度为0的任务
        while (!zeroDeque.isEmpty()) {
            Class<? extends Startup> aClass = zeroDeque.poll();
            Startup<?> startup = startupMap.get(aClass);
            //加入到排序结果中
            result.add(startup);
            //删除该0入度的任务
            if (startupChildrenMap.containsKey(aClass)) {
                //拿到当前任务的第一后续任务列表
                List<Class<? extends Startup>> list = startupChildrenMap.get(aClass);
                for (Class<? extends Startup> su : list) {
                    //遍历当前任务第一后续列表
                    //拿到第一后续相应的入度 且减一
                    Integer num = inDegreeMap.get(su);
                    //更新入度表
                    inDegreeMap.put(su, num - 1);
                    if (num - 1 == 0) { //入度为0 进入0入度队列
                        zeroDeque.offer(su);
                    }
                }
            }
        }

        return new StartupSortStore(result, startupMap, startupChildrenMap);
    }
}

package com.rocky.androidstartup.startup;

import com.rocky.androidstartup.task.Startup;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : rocky
 *     time   : 2022/03/14
 *     des    : 此类是一个数据类
 *              记录了所有任务
 * </pre>
 */
public class StartupSortStore {
    //排序的结果
    List<Startup<?>> result;
    //原始任务
    Map<Class<? extends Startup>, Startup<?>> startupMap;
    //当前任务的子任务  即 当前任务所依赖的任务
    Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap;


    public StartupSortStore(List<Startup<?>> result, Map<Class<? extends Startup>, Startup<?>> startupMap, Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap) {
        this.result = result;
        this.startupMap = startupMap;
        this.startupChildrenMap = startupChildrenMap;
    }

    public List<Startup<?>> getResult() {
        return result;
    }

    public void setResult(List<Startup<?>> result) {
        this.result = result;
    }

    public Map<Class<? extends Startup>, Startup<?>> getStartupMap() {
        return startupMap;
    }

    public void setStartupMap(Map<Class<? extends Startup>, Startup<?>> startupMap) {
        this.startupMap = startupMap;
    }

    public Map<Class<? extends Startup>, List<Class<? extends Startup>>> getStartupChildrenMap() {
        return startupChildrenMap;
    }

    public void setStartupChildrenMap(Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap) {
        this.startupChildrenMap = startupChildrenMap;
    }
}

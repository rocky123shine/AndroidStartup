package com.rocky.androidstartup.startup;

/**
 * 任务执行结果原样保存
 * @param <T>
 */
public class Result<T> {

    public T data;

    public Result(T data) {
        this.data = data;
    }
}

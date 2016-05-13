package com.admin.file.manager;

public interface TaskAction<P, R> {
    public R obtainData(Task<P, R> task, P parameter) throws Exception;
}
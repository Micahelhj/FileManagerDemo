package com.admin.file.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * 线性链表任务容器
 */
public final class TaskGroupLinked extends TaskGroup {

    ArrayList<Task> tasks = new ArrayList<Task>();

    Task current_task;

    int length;

    @Override
    public void shutDownExecute() {
        // TODO Auto-generated method stub
        if (current_task != null) {
            current_task.shutDownExecute();
        }
        setWithout();
        tasks = null;

    }

    @Override
    public Object cacheData(Object parameter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object obtainData(Task task, Object parameter) throws Exception {
        // TODO Auto-generated method stub
        length = tasks.size();
        for (int i = 0; i < length; i++) {
            if (status != TaskStatus.without) {
                current_task = tasks.get(i);
                current_task.run();
                if (onProgressListen != null) {
                    onProgressListen.onProgress(current_task, (int) (100.0f * i / length),
                            current_task.getResult());
                }
            }
        }

        return tasks;
    }

    @Override
    public TaskContainer addTask(Task task) {
        if (task == null) {
            return this;
        }

        if (task.status != TaskStatus.untreated) {
            return this;
        }
        tasks.add(task);
        return this;
    }

    @Override
    public void notifyTaskGroup(Task task) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Task> getTaskList() {
        // TODO Auto-generated method stub
        return tasks;
    }

//	// 置空引用
//	@Override
//	public void cacheClear() {
//		// TODO Auto-generated method stub
//		super.cacheClear();
//		tasks = null;
//	}

}

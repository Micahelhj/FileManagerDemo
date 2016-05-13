package com.admin.file.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.test.suitebuilder.annotation.LargeTest;

@LargeTest
public class TaskGroupAsyn extends TaskGroup {


    ArrayList<Task> list = new ArrayList<Task>();
    int length = 0;
    int overConunt = 0;

    @Override
    public List<Task> getTaskList() {
        // TODO Auto-generated method stub
        return list;
    }

    @Override
    public TaskGroup addTask(Task task) {
        // TODO Auto-generated method stub
        task.setTaskGroup(this);
        list.add(task);
        return this;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        length = list.size();
        Iterator<Task> it = list.iterator();
        while (it.hasNext()) {
            it.next().start();
        }
        System.out.println("任务队列开始");
        super.start();
    }

    @Override
    public void notifyTaskGroup(Task task) {
        // TODO Auto-generated method stub
        System.out.println("任务队列的" + task.getTaskID() + "完成");
        synchronized (task) {
            if (list.indexOf(task) != -1) {
                overConunt++;
                if (overConunt == length) {

                    if (this.getOnGroupFinsh() != null) {
                        new OnGroupFinshRunnable(this);
                    }
                }
            }
        }

    }

    @Override
    public void shutDownExecute() {
        // TODO Auto-generated method stub
        Iterator<Task> it = list.iterator();
        while (it.hasNext()) {
            it.next().setWithout();
        }
    }

    @Override
    public Object cacheData(Object parameter) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Object obtainData(Task task, Object parameter) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }


}

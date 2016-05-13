package com.admin.file.manager;

import java.util.List;

public abstract class TaskGroup extends TaskContainer {

    public interface OnGroupFinsh {
        void onGroupFinsh(List<Task> task);
    }

    OnGroupFinsh onGroupFinsh;

    class OnGroupFinshRunnable implements Runnable {
        OnGroupFinshRunnable(TaskGroup taskGroup) {
            super();
            this.taskGroup = taskGroup;
            publicModuleHandler.post(this);
        }

        protected TaskGroup taskGroup;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("任务队列任务完成");
            taskGroup.getOnGroupFinsh().onGroupFinsh(taskGroup.getTaskList());
        }
    }


    public OnGroupFinsh getOnGroupFinsh() {
        return onGroupFinsh;
    }

    public void setOnGroupFinsh(OnGroupFinsh onGroupFinsh) {
        this.onGroupFinsh = onGroupFinsh;
    }

    public abstract void notifyTaskGroup(Task task);

    public abstract List<Task> getTaskList();

}

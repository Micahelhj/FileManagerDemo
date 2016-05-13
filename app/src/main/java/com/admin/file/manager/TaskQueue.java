package com.admin.file.manager;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;


public class TaskQueue implements Runnable, Task.OnSystemFinishListen {
    static String debug = "TaskQueue";
    @SuppressWarnings("unchecked")
    // 在等待的任务队列
    static LinkedList<Task> tasks_wait = new LinkedList<Task>();

    public static class TaskQueueExpection extends Exception {
        TaskQueueExpection(String detailMessage) {
            super(detailMessage);
            // TODO Auto-generated constructor stub
        }

    }

    ;

    // 正在执行的任务
    static ArrayList<Task> tasks_running = new ArrayList<Task>();
    // 是否持续运行
    public static boolean isRun = true;
    // runnable保证线程安全
    private static TaskQueue runnable = new TaskQueue();
    ;
    // 最大线程数
    static int ThreadMaxNum = 1;

    public static TaskQueue getRunnable() {
        return runnable;
    }

    // 如果队列线程为空或者停止则重新开启
    public static void serivesRun() {
        // TODO Auto-generated method stub
        boolean isCanSeriver = false;
        synchronized (tasks_running) {
            isCanSeriver = tasks_running.size() < ThreadMaxNum;
        }
        runnable.run();
    }

    //获取正在执行的任务数
    public static int getRunningTaskCount() {
        synchronized (TaskQueue.tasks_running) {
            return TaskQueue.tasks_running.size();
        }
    }

    //设置最大任务数
    public static void setThreadMaxNum(int num) {
        TaskQueue.ThreadMaxNum = num < 1 ? 1 : num > 100 ? 100 : num;
    }

    // 线程锁 如果等待队列的任务数不为空，并且当前线程数字少于最大线程数
    public static boolean taskRun() {
        synchronized (tasks_wait) {
            synchronized (tasks_running) {
                return !tasks_wait.isEmpty()
                        && tasks_running.size() < ThreadMaxNum;
            }
        }
    }

    //开启新线程
    public void run() {
        // 线程锁 如果等待队列的任务数不为空，并且当前线程数字少于最大线程数
        Task newTask;
        while ((newTask = getWaittingTask()) != null) {
            System.err.println("开启新线程处理一个新任务，ID：" + newTask.getTaskID());
            newTask.setOnSystemFinishListen(runnable);
            newTask.threadRun();
            newTask = null;
        }

    }

    //递归 避免新开线程   唤醒等待中的任务 但此方案会造成java.lang.StackOverflowError
    void notifyWaitingTask() {
        Task newTask;
        while ((newTask = getWaittingTask()) != null) {
            System.err.println("唤醒旧线程处理一个新任务，ID：" + newTask.getTaskID());
            newTask.setOnSystemFinishListen(runnable);
            newTask.run();
            newTask = null;
        }

    }

    private Task getWaittingTask() {
        Task t = null;
        //测试
        while (isRun && taskRun()) {
            // 添加带执行中动态数组中
            synchronized (tasks_wait) {
                // 从等待任务的队列中获取并移除此列表的头（第一个元素）
                t = tasks_wait.poll();
                Log.i(debug, "队列的任务数" + tasks_wait.size());
                // 如果h为空则从队列重新取对象或者任务绑定的状态变化了
                if (t == null || t.status == Task.TaskStatus.without) {
                    Log.i(debug, "任务取消 编号" + t != null ? String.valueOf(t.getTaskID()) : "空任务");
                    continue;
                }
            }
            synchronized (tasks_running) {
                tasks_running.add(t);
            }
            Log.i(debug, "正在执行任务数" + tasks_running.size() + "/上限"
                    + ThreadMaxNum);

            return t;
        }
        return t;
    }


    @Override
    public void OnSystemFinish(Task t, Object data) {
        // TODO Auto-generated method stub
        synchronized (tasks_running) {
            // 从处理中的动态数组中移除此任务
            tasks_running.remove(t);
            Log.i(debug, "执行队列中移除任务taskid=" + t.taskID);
            // 通知执行后续未处理的任务
            Log.i(debug, "正在执行任务数" + tasks_running.size() + "/上限"
                    + ThreadMaxNum);

            if (t.getTaskObservable() != null) {
                t.getTaskObservable().deleteObserver(t);
            }
            // 移除此名字映射
            if (t.getSingletonName() != null) {
                Task.getNameTask().remove(t.getSingletonName());
            }
            //通知任务所在的任务组
            t.notifyTaskGroup();
        }


        //onFinishListen后回收
//		if(t.onFinishListen==null){
//		   t.cacheClear();
//		}
    }

}

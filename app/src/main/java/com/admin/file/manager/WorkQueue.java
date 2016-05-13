package com.admin.file.manager;

import java.util.LinkedList;

/**
 * 项目名称：FileManagerDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：16/5/13 下午9:27
 * 修改人：Michael-hj
 * 修改时间：16/5/13 下午9:27
 * 修改备注：
 */
public class WorkQueue {
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;

    public WorkQueue(int nThreads) {
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable r) {
        synchronized (queue) {
            queue.addLast(r);
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            Runnable r;
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    r = (Runnable) queue.removeFirst();
                }
                // If we don't catch RuntimeException,
                // the pool could leak threads
                try {
                    r.run();
                } catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
    }
}
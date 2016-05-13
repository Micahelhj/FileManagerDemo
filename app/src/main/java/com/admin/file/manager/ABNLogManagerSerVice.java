package com.admin.file.manager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称：FileManagerDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：16/5/13 下午11:29
 * 修改人：Michael-hj
 * 修改时间：16/5/13 下午11:29
 * 修改备注：
 */
public class ABNLogManagerSerVice extends Service {

    private static final LinkedList queue = null;

    //创建一个可重用固定线程数的线程池
    private static ExecutorService pool = null;

    private static String CURR_INSTALL_LOG_NAME = "Log.txt"; // 如果当前的日志写在内存中，记录当前的日志文件名称

    @Override
    public void onCreate() {
        super.onCreate();
        initPoolWorker();
        //====================设置广播接收器======================================
        IntentFilter filter = new IntentFilter();
        filter.addAction(ABNLogManager.LOG_SERVICE_BROADSERVICE_ACTION);
        registerReceiver(broadcastReceiver, filter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ABNLogManager.LOG_SERVICE_BROADSERVICE_ACTION.equals(action)) {
                ABNLogEntity logEntity = (ABNLogEntity) intent.getSerializableExtra(ABNLogManager.LOG_SERVICE_INTEXT_KEY);
                if (logEntity != null && logEntity.getDir() != null
                        && logEntity.getData() != null
                        && logEntity.getInputStreamDta() != null) {
                    if (logEntity.getType() == 0)
                        execute(new ABNLogEntity(logEntity.getDir(), logEntity.getInputStreamDta()));
                    else
                        execute(new ABNLogEntity(logEntity.getDir(), logEntity.getData()));
                }

            }
        }
    };

    /**
     * 初始化sing线程池并加入一个线程
     */
    private void initPoolWorker() {
        pool = Executors.newSingleThreadExecutor();
        pool.execute(new PoolWorker());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 将日志信息提交至队列中
     *
     * @param t
     */
    public void execute(ABNLogEntity t) {
        synchronized (queue) {
            queue.addLast(t);
            queue.notify();
        }
    }

    /**
     * 工作队列实现
     */
    private class PoolWorker implements Runnable {

        public void run() {
            ABNLogEntity firstLogEntity;
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    firstLogEntity = (ABNLogEntity) queue.removeFirst();
                    if (firstLogEntity.getType() == 0)
                        recordLog2Native(firstLogEntity.getDir(), firstLogEntity.getInputStreamDta());
                    else
                        recordLog2Native(firstLogEntity.getDir(), firstLogEntity.getData());
                }
                // If we don't catch RuntimeException,
                // the pool could leak threads
                try {
                    run();
                } catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
    }

    /**
     * 记录日志到本地
     *
     * @param data
     */
    private void recordLog2Native(String dir, InputStream data) {
        ABNFileUtil.writeFile(ABNFileManager.getFile(ABNFileManager.getNormalLogDownloadDir(this) + File.separator + dir, CURR_INSTALL_LOG_NAME), data, true);
    }

    /**
     * 记录日志到本地
     *
     * @param content
     */
    private void recordLog2Native(String dir, String content) {
        ABNFileUtil.writeFile(ABNFileManager.getNormalLogDownloadDir(this) + File.separator + dir + File.separator + CURR_INSTALL_LOG_NAME, content, true);
    }
}

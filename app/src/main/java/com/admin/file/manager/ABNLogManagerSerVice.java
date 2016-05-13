package com.admin.file.manager;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
public class ABNLogManagerSerVice extends IntentService {

    private static final LinkedList queue = null;

    //创建一个可重用固定线程数的线程池
    private static ExecutorService pool = null;

    public ABNLogManagerSerVice(String name) {
        super("ABNLogManagerSerVice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            intentDtata(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPoolWorker();
        //====================设置广播接收器======================================
        IntentFilter filter = new IntentFilter();
        filter.addAction(ABNLogManager.LOG_SERVICE_BROADSERVICE_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 处理广播
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ABNLogManager.LOG_SERVICE_BROADSERVICE_ACTION.equals(action)) {
                intentDtata(intent);
            }
        }
    };

    /**
     * 处理Intent
     *
     * @param intent
     */
    private void intentDtata(Intent intent) {
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


    private static final String TAG = "LogService";

    private static final int MEMORY_LOG_FILE_MAX_SIZE = 10 * 1024 * 1024; // 内存中日志文件最大值，10M
    private static final int MEMORY_LOG_FILE_MONITOR_INTERVAL = 10 * 60 * 1000; // 内存中的日志文件大小监控时间间隔，10分钟
    private static final int SDCARD_LOG_FILE_SAVE_DAYS = 7; // sd卡中日志文件的最多保存天数

    private String LOG_PATH_MEMORY_DIR; // 日志文件在内存中的路径(日志文件在安装目录中的路径)
    private String LOG_PATH_SDCARD_DIR; // 日志文件在sdcard中的路径
    @SuppressWarnings("unused")
    private String LOG_SERVICE_LOG_PATH; // 本服务产生的日志，记录日志服务开启失败信息

    private final int SDCARD_TYPE = 0; // 当前的日志记录类型为存储在SD卡下面
    private final int MEMORY_TYPE = 1; // 当前的日志记录类型为存储在内存中
    private int CURR_LOG_TYPE = SDCARD_TYPE; // 当前的日志记录类型

    private String CURR_INSTALL_LOG_NAME; // 如果当前的日志写在内存中，记录当前的日志文件名称

    private String logServiceLogName = "Log.log";// 本服务输出的日志文件名称
    private SimpleDateFormat myLogSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private OutputStreamWriter writer;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");// 日志名称格式

    private Process process;

    private PowerManager.WakeLock wakeLock;

    /*
     * 是否正在监测日志文件大小； 如果当前日志记录在SDcard中则为false 如果当前日志记录在内存中则为true
     */
    private boolean logSizeMoniting = false;

    private static String MONITOR_LOG_SIZE_ACTION = "MONITOR_LOG_SIZE"; // 日志文件监测action
    private static String SWITCH_LOG_FILE_ACTION = "SWITCH_LOG_FILE_ACTION"; // 切换日志文件action

    /**
     * 部署日志切换任务，每天凌晨切换日志文件
     */
    private void deploySwitchLogFileTask() {
        Intent intent = new Intent(SWITCH_LOG_FILE_ACTION);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 部署任务
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, sender);
    }

    /**
     * 检查日志文件大小是否超过了规定大小 如果超过了重新开启一个日志收集进程
     */
    private void checkLogSize() {
        if (CURR_INSTALL_LOG_NAME != null && !"".equals(CURR_INSTALL_LOG_NAME)) {
            String path = LOG_PATH_MEMORY_DIR + File.separator
                    + CURR_INSTALL_LOG_NAME;
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            Log.d(TAG, "checkLog() ==> The size of the log is too big?");
            if (file.length() >= MEMORY_LOG_FILE_MAX_SIZE) {
                Log.d(TAG, "The log's size is too big!");
            }
        }
    }

    /**
     * 删除内存下过期的日志
     */
    private void deleteSDcardExpiredLog() {
        File file = new File(LOG_PATH_SDCARD_DIR);
        if (file.isDirectory()) {
            File[] allFiles = file.listFiles();
            for (File logFile : allFiles) {
                String fileName = logFile.getName();
                if (logServiceLogName.equals(fileName)) {
                    continue;
                }
//                String createDateInfo = getFileNameWithoutExtension(fileName);
//                if (canDeleteSDLog(createDateInfo)) {
//                    logFile.delete();
//                    Log.d(TAG, "delete expired log success,the log path is:"
//                            + logFile.getAbsolutePath());
//
//                }
            }
        }
    }

    /**
     * 判断sdcard上的日志文件是否可以删除
     *
     * @param createDateStr
     * @return
     */
    public boolean canDeleteSDLog(String createDateStr) {
        boolean canDel = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1 * SDCARD_LOG_FILE_SAVE_DAYS);// 删除7天之前日志
        Date expiredDate = calendar.getTime();
        try {
            Date createDate = sdf.parse(createDateStr);
            canDel = createDate.before(expiredDate);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
            canDel = false;
        }
        return canDel;
    }

    /**
     * 删除内存中的过期日志，删除规则： 除了当前的日志和离当前时间最近的日志保存其他的都删除
     */
    private void deleteMemoryExpiredLog() {
        File file = new File(LOG_PATH_MEMORY_DIR);
        if (file.isDirectory()) {
            File[] allFiles = file.listFiles();
//            Arrays.sort(allFiles, new FileComparator());
            for (int i = 0; i < allFiles.length - 2; i++) { // "-2"保存最近的两个日志文件
                File _file = allFiles[i];
                if (logServiceLogName.equals(_file.getName())
                        || _file.getName().equals(CURR_INSTALL_LOG_NAME)) {
                    continue;
                }
                _file.delete();
                Log.d(TAG, "delete expired log success,the log path is:"
                        + _file.getAbsolutePath());
            }
        }
    }

}

package com.admin.file.manager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * 项目名称：FileManagerDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：16/5/13 下午11:29
 * 修改人：Michael-hj
 * 修改时间：16/5/13 下午11:29
 * 修改备注：
 */
public class ABLogManagerSerVice extends Service {

    private static final String TAG = "LogService";
    private static LinkedList queue = null;
    private static final int MEMORY_LOG_FILE_MAX_SIZE = 10 * 1024 * 1024; // 内存中日志文件最大值，10M
    private static final int SDCARD_LOG_FILE_SAVE_DAYS = 7; // sd卡中日志文件的最多保存天数
    private String LOG_PATH_MEMORY_DIR; // 日志文件在内存中的路径(日志文件在安装目录中的路径)
    private String CURR__LOG_NAME; // 如果当前的日志写在内存中，记录当前的日志文件名称
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 日志名称格式


    //创建一个可重用固定线程数的线程池
    private static ExecutorService pool = null;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = new LinkedList();
        LOG_PATH_MEMORY_DIR = ABFileManager.getNormalLogDownloadDir(this);
        CURR__LOG_NAME = sdf.format(new Date()) + ".log";// 日志文件名称
        register();
        initPoolWorker();
    }

    private void register() {
        //====================设置广播接收器======================================
        IntentFilter filter = new IntentFilter();
        filter.addAction(ABLogManager.LOG_SERVICE_BROADSERVICE_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 处理广播
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ABLogManager.LOG_SERVICE_BROADSERVICE_ACTION.equals(action)) {
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
        ABLogManager.ABNLogEntity logEntity = (ABLogManager.ABNLogEntity) intent.getSerializableExtra(ABLogManager.LOG_SERVICE_INTEXT_KEY);
        if (logEntity != null && logEntity.getDir() != null && logEntity.getData() != null
                && (logEntity.getInputStreamDta() != null || logEntity.getData() != null)) {

            LOG_PATH_MEMORY_DIR = ABFileManager.getNormalLogDownloadDir(this) + File.separator + logEntity.getDir();
            if (logEntity.getType() == 0)
                execute(new ABLogManager.ABNLogEntity(logEntity.getDir(), logEntity.getInputStreamDta()));
            else
                execute(new ABLogManager.ABNLogEntity(logEntity.getDir(), logEntity.getData()));
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
    public void execute(ABLogManager.ABNLogEntity t) {
        if (queue == null)
            queue = new LinkedList();
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
            ABLogManager.ABNLogEntity firstLogEntity;
            if (queue == null)
                queue = new LinkedList();
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    checkLogSize();
                    deleteExpiredLog();
                    deleteMemoryExpiredLog();
                    try {
                        sleep(1000);// 休眠，创建文件，然后处理文件，不然该文件还没创建，会影响文件删除
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    firstLogEntity = (ABLogManager.ABNLogEntity) queue.removeFirst();
                    if (firstLogEntity.getType() == 0)
                        recordLog2Native(firstLogEntity.getDir(), firstLogEntity.getInputStreamDta());
                    else
                        recordLog2Native(firstLogEntity.getDir(), firstLogEntity.getData());
                }
                try {
                    run();
                } catch (RuntimeException e) {
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
        String dataString = "";
        try {
            dataString = ABFileUtil.inputStream2String(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeFile(ABFileManager.getNormalLogDownloadDir(this) + File.separator + dir + File.separator + CURR__LOG_NAME, dataString, true);
    }

    /**
     * 记录日志到本地
     *
     * @param content
     */
    private void recordLog2Native(String dir, String content) {
        writeFile(ABFileManager.getNormalLogDownloadDir(this) + File.separator + dir + File.separator + CURR__LOG_NAME, content, true);
    }

    /**
     * 检查日志文件大小是否超过了规定大小 如果超过了重新开启一个日志收集进程
     */
    private void checkLogSize() {
        if (CURR__LOG_NAME != null && !"".equals(CURR__LOG_NAME)) {
            String path = LOG_PATH_MEMORY_DIR + File.separator
                    + CURR__LOG_NAME;
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
     * 去除文件的扩展类型（.log）
     *
     * @param fileName
     * @return
     */
    private String getFileNameWithoutExtension(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";
        if (!fileName.contains("."))
            return "";
        return fileName.substring(0, fileName.indexOf("."));
    }

    /**
     * 删除内存下过期的日志
     */
    private void deleteExpiredLog() {
        File file = new File(LOG_PATH_MEMORY_DIR);
        if (file.isDirectory()) {
            File[] allFiles = file.listFiles();
            for (File logFile : allFiles) {
                String fileName = logFile.getName();
                String createDateInfo = getFileNameWithoutExtension(fileName);
                if (canDeleteLog(createDateInfo)) {
                    logFile.delete();
                    Log.d(TAG, "delete expired log success,the log path is:"
                            + logFile.getAbsolutePath());

                }
            }
        }
    }

    /**
     * 判断sdcard上的日志文件是否可以删除
     *
     * @param createDateStr
     * @return
     */
    public boolean canDeleteLog(String createDateStr) {
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
            Arrays.sort(allFiles, new FileComparator());
            for (int i = 0; i < allFiles.length - 2; i++) { // "-2"保存最近的两个日志文件
                File _file = allFiles[i];
                if (_file.getName().equals(CURR__LOG_NAME)) {
                    continue;
                }
                _file.delete();
                Log.d(TAG, "delete expired log success,the log path is:"
                        + _file.getAbsolutePath());
            }
        }
    }

    class FileComparator implements Comparator<File> {
        public int compare(File file1, File file2) {
            String createInfo1 = getFileNameWithoutExtension(file1.getName());
            String createInfo2 = getFileNameWithoutExtension(file2.getName());

            try {
                Date create1 = sdf.parse(createInfo1);
                Date create2 = sdf.parse(createInfo2);
                if (create1.before(create2)) {
                    return -1;
                } else {
                    return 1;
                }
            } catch (ParseException e) {
                return 0;
            }
        }
    }

    /**
     * write file
     *
     * @param filePath 路径
     * @param content  上下文
     * @param append   is append, if true, write to the end of file, else clear
     *                 content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            ABFileUtil.makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.write("\r\n");//换行
            fileWriter.write("\r\n");//换行
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            ABIOUtil.close(fileWriter);
        }
    }
}

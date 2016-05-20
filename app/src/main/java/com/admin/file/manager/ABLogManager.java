package com.admin.file.manager;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
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
 * @author Michael_hj
 * @ClassName: ABLogManager
 * @Description: TODO(日志管理工具)
 * @date 2016年5月18日 上午10:52:19
 */
public class ABLogManager {
    private static Context context;
    private static final String TAG = "ABLogManager";
    private static LinkedList queue = null;
    private static final int MEMORY_LOG_FILE_MAX_SIZE = 10 * 1024 * 1024; // 内存中日志文件最大值，10M
    private static final int SDCARD_LOG_FILE_SAVE_DAYS = 7; // sd卡中日志文件的最多保存天数
    private static String LOG_PATH_MEMORY_DIR; // 日志文件在内存中的路径(日志文件在安装目录中的路径)
    private static String CURR__LOG_NAME; // 如果当前的日志写在内存中，记录当前的日志文件名称
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 日志名称格式
    private static String fileType = ".txt";
    //创建一个可重用固定线程数的线程池
    private static ExecutorService pool = null;
    private static WakeLock wakeLock;

    public static void initLogManager(Context c) {
        context = c;
        initPoolWorker();
        LOG_PATH_MEMORY_DIR = ABFileManager.getNormalLogDownloadDir(c);
        CURR__LOG_NAME = sdf.format(new Date()) + fileType;// 日志文件名称
        PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    /**
     * 记录日志到本地
     *
     * @param dir
     * @param data
     */
    public static void recordLog(String dir, InputStream data) {
        String dataString = "";
        try {
            dataString = ABFileUtil.inputStream2String(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (queue == null || pool == null)
            initPoolWorker();
        execute(new ABNLogEntity(dir, dataString));
    }

    /**
     * 记录日志到本地
     *
     * @param dir
     * @param data
     */
    public static void recordLog(String dir, String data) {
        if (queue == null || pool == null)
            initPoolWorker();
        execute(new ABNLogEntity(dir, data));
    }

    /**
     * 初始化sing线程池并加入一个线程
     */
    private static void initPoolWorker() {
        queue = new LinkedList();
        pool = Executors.newSingleThreadExecutor();
        pool.execute(new PoolWorker());
    }

    /**
     * 将日志信息提交至队列中
     *
     * @param t
     */
    public static void execute(ABNLogEntity t) {
        if (queue == null || pool == null)
            initPoolWorker();
        synchronized (queue) {
            queue.addLast(t);
            queue.notify();
        }
    }

    /**
     * 工作队列实现
     */
    private static class PoolWorker implements Runnable {
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
                    wakeLock.acquire(); // 唤醒手机
                    checkLogSize();
                    deleteExpiredLog();
                    deleteMemoryExpiredLog();
                    try {
                        sleep(2000);// 休眠，创建文件，然后处理文件，不然该文件还没创建，会影响文件删除
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    firstLogEntity = (ABNLogEntity) queue.removeFirst();
                    writeFile(ABFileManager.getNormalLogDownloadDir(context) + File.separator + firstLogEntity.getDir() + File.separator + CURR__LOG_NAME, firstLogEntity.getData(), true);
                    wakeLock.release(); // 释放
                }
                try {
                    run();
                } catch (RuntimeException e) {
                }
            }
        }
    }

    /**
     * 检查日志文件大小是否超过了规定大小 如果超过了重新开启一个日志收集进程
     */
    private static void checkLogSize() {
        if (CURR__LOG_NAME != null && !"".equals(CURR__LOG_NAME)) {
            String path = LOG_PATH_MEMORY_DIR + File.separator + CURR__LOG_NAME;
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
    private static String getFileNameWithoutExtension(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";
        if (!fileName.contains("."))
            return "";
        return fileName.substring(0, fileName.indexOf("."));
    }

    /**
     * 删除内存下过期的日志
     */
    private static void deleteExpiredLog() {
        File file = new File(LOG_PATH_MEMORY_DIR);
        if (file.isDirectory()) {
            File[] allFiles = file.listFiles();
            for (File logFile : allFiles) {
                String fileName = logFile.getName();
                String createDateInfo = getFileNameWithoutExtension(fileName);
                if (!TextUtils.isEmpty(createDateInfo) && canDeleteLog(createDateInfo)) {
                    logFile.delete();
                    Log.d(TAG, "delete expired log success,the log path is:" + logFile.getAbsolutePath());
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
    public static boolean canDeleteLog(String createDateStr) {
        boolean canDel = false;
        if (TextUtils.isEmpty(createDateStr))
            return false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1 * SDCARD_LOG_FILE_SAVE_DAYS);// 删除7天之前日志
        Date expiredDate = calendar.getTime();
        try {
            Date createDate = sdf.parse(createDateStr);
            canDel = createDate.before(expiredDate);
        } catch (ParseException e) {
            canDel = false;
        }
        return canDel;
    }

    /**
     * 删除内存中的过期日志，删除规则： 除了当前的日志和离当前时间最近的日志保存其他的都删除
     */
    private static void deleteMemoryExpiredLog() {
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
            }
        }
    }

    static class FileComparator implements Comparator<File> {
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
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        FileOutputStream outputStream = null;
        OutputStreamWriter write = null;
        Date date = new Date();
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        try {
            File f = new File(filePath);
            ABFileUtil.makeDirs(filePath);
            outputStream = new FileOutputStream(f, append);
            write = new OutputStreamWriter(outputStream, "UTF-8");
            write.write("= " + new Timestamp(System.currentTimeMillis()));
            write.write("\r\n");//换行
            write.write(content);
            write.write("\r\n");//换行
            write.write("----------------------------------------------------------------");//换行
            write.write("\r\n");//换行
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        } finally {
            date = null;
            try {
                if (write != null)
                    write.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }

    private static final String date_format = "yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

    public static DateFormat getDateFormat() {
        DateFormat df = threadLocal.get();
        if (df == null) {
            df = new SimpleDateFormat(date_format);
            threadLocal.set(df);
        }
        return df;
    }

    public static String formatDate(Date date) throws ParseException {
        return getDateFormat().format(date);
    }

    public static Date parse(String strDate) throws ParseException {
        return getDateFormat().parse(strDate);
    }

    /**
     * 日志信息实体类
     */
    public static class ABNLogEntity implements Serializable {
        private String dir;
        /**
         * 传值的类型 0 InputStream 1 String
         */
        private String data;

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public ABNLogEntity() {
        }

        public ABNLogEntity(String dir, String data) {
            this.dir = dir;
            this.data = data;
        }
    }
}

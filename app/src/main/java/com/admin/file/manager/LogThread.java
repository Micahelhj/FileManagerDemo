package com.admin.file.manager;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.InputStream;

/**
 * 项目名称：FileManagerDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/13 0013 15:57
 * 修改人：Michael-hj
 * 修改时间：2016/5/13 0013 15:57
 * 修改备注：
 */
public class LogThread extends Thread {

    private static Context context;
    /**
     * 开始处理
     */
    public final static int THREAD_BEGIN = 1;
    /**
     * 处理结束
     */
    public final static int THREAD_FINISHED = 2;

    private String CURR_INSTALL_LOG_NAME = "Log.txt"; // 如果当前的日志写在内存中，记录当前的日志文件名称
    //文件夹名
    private String dir;
    //是否线程已启动
    private boolean isStarted = false;

    public LogThread(Context context, String dir) {
        this.dir = dir;
        this.context = context;
    }

    /**
     * 开始下载任务
     */
    @Override
    public void run() {
        isStarted = true;
        try {

        } catch (Exception e) {
            e.printStackTrace();
            //TODO:建议这里发送下载失败的消息
        } finally {
        }
    }
}

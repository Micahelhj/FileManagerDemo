package com.admin.file.manager;


import android.os.Handler;
import android.os.Message;

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

    /**
     * 开始处理
     */
    public final static int THREAD_BEGIN = 1;
    /**
     * 处理结束
     */
    public final static int THREAD_FINISHED = 2;
    //下载的文件大小
    private long fileLength;
    //文件的保存路径
    private String filePath;
    //是否线程已启动
    private boolean isStarted = false;

    private Handler mHandler;


    public LogThread(String filePath, Handler mHandler) {
        this.filePath = filePath;
        this.mHandler = mHandler;
    }

    /**
     * 开始下载任务
     */
    @Override
    public void run() {
        isStarted = true;
        try {

            //发送处理完毕的消息
            Message msg = new Message();
            msg.what = THREAD_FINISHED;
            mHandler.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
            //TODO:建议这里发送下载失败的消息
        } finally {
        }
    }


    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

}

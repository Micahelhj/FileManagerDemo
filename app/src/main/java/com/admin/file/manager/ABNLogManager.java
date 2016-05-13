package com.admin.file.manager;

import android.content.Context;

import java.io.File;
import java.io.InputStream;

/**
 * 项目名称：FileManagerDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/13 0013 11:21
 * 修改人：Michael-hj
 * 修改时间：2016/5/13 0013 11:21
 * 修改备注：
 */
public class ABNLogManager {
    private static Context context;
    public static ABNLogManager application;

    public ABNLogManager(Context c) {
        context = c;
    }

    public static ABNLogManager getInstance(Context c) {
        if (application == null)
            application = new ABNLogManager(c);
        context = c;
        return application;
    }

    private String CURR_INSTALL_LOG_NAME = "Log.txt"; // 如果当前的日志写在内存中，记录当前的日志文件名称

    /**
     * 记录日志到本地
     *
     * @param data
     */
    public void recordLog2Native(String dir, InputStream data) {
        ABNFileUtil.writeFile(ABNFileManager.getFile(ABNFileManager.getNormalLogDownloadDir(context) + File.separator + dir, CURR_INSTALL_LOG_NAME), data, true);
    }

    /**
     * 记录日志到本地
     *
     * @param content
     */
    public void recordLog2Native(String dir, String content) {
        ABNFileUtil.writeFile(ABNFileManager.getNormalLogDownloadDir(context) + File.separator + dir + File.separator + CURR_INSTALL_LOG_NAME, content, true);
    }
}
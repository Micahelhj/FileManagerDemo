package com.admin.file.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.InputStream;
import java.util.List;

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

    public static String LOG_SERVICE_BROADSERVICE_ACTION = "LogService";
    public static String LOG_SERVICE_INTEXT_KEY = "LogService";

    private static Context context;

    public static ABNLogManager application;

    public ABNLogManager(Context c) {
        isRun = isServiceRunning("");
        context = c;
    }

    private boolean isRun = false;

    public static ABNLogManager getInstance(Context c) {
        if (application == null)
            application = new ABNLogManager(c);
        context = c;
        return application;
    }

    /**
     * 记录日志到本地
     *
     * @param dir
     * @param data
     */
    public void recordLog(String dir, InputStream data) {
        if (isRun)
            sendLogService(new ABNLogEntity(dir, data));
        else
            startLogService(new ABNLogEntity(dir, data));
    }

    public void recordLog(String dir, String data) {
        if (isRun)
            sendLogService(new ABNLogEntity(dir, data));
        else
            startLogService(new ABNLogEntity(dir, data));
    }

    /**
     * 发送日志信息广播
     *
     * @param logEntity
     */
    private void sendLogService(ABNLogEntity logEntity) {
        Intent intent = new Intent(LOG_SERVICE_BROADSERVICE_ACTION);
        intent.putExtra(LOG_SERVICE_INTEXT_KEY, logEntity);
        context.sendBroadcast(intent);
    }

    /**
     * 启动Log服务并传入日志信息
     *
     * @param logEntity
     */
    private void startLogService(ABNLogEntity logEntity) {
        Intent intent = new Intent(context, ABNLogManagerSerVice.class);
        intent.putExtra(LOG_SERVICE_INTEXT_KEY, logEntity);
        context.startService(intent);
    }

    /**
     * 判断LogService是否正在运行
     *
     * @param serviceClassName
     * @return
     */
    public static boolean isServiceRunning(String serviceClassName) {
        if (TextUtils.isEmpty(serviceClassName))
            return false;
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }
}
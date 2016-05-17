package com.admin.file.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.InputStream;
import java.io.Serializable;
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
public class ABLogManager {
    private static Context context;
    public static String LOG_SERVICE_BROADSERVICE_ACTION = "log_service_broadservice_action";
    public static String LOG_SERVICE_INTEXT_KEY = "log_service_intext_key";
    private static String servicePath = "com.dfwd.wlkt.service.ABLogManagerSerVice";

    public static void initLogManager(Context c) {
        context = c;
    }

    /**
     * 记录日志到本地
     *
     * @param dir
     * @param data
     */
    public static void recordLog(String dir, InputStream data) {
        if (isServiceRunning(servicePath))
            sendLogService(new ABNLogEntity(dir, data));
        else
            ABLogUtil.w("RuntimeException:请启动日志记录服务" + new RuntimeException());
    }

    public static void recordLog(String dir, String data) {
        if (isServiceRunning(servicePath))
            sendLogService(new ABNLogEntity(dir, data));
        else
            ABLogUtil.w("RuntimeException:请启动日志记录服务" + new RuntimeException());
    }

    /**
     * 发送日志信息广播
     *
     * @param logEntity
     */
    private static void sendLogService(ABNLogEntity logEntity) {
        if (context == null) {
            ABLogUtil.e("请初始化LogManager");
        }
        Intent intent = new Intent(LOG_SERVICE_BROADSERVICE_ACTION);
        intent.putExtra(LOG_SERVICE_INTEXT_KEY, logEntity);
        context.sendBroadcast(intent);
    }

    /**
     * 判断LogService是否正在运行
     *
     * @param serviceClassName
     * @return
     */
    private static boolean isServiceRunning(String serviceClassName) {
        if (context == null) {
            ABLogUtil.e("请初始化LogManager");
        }
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

    /**
     * 日志信息实体类
     */
    public static class ABNLogEntity implements Serializable {

        private String dir;

        /**
         * 传值的类型 0 InputStream   1 String
         */
        private int type;
        private String data;
        private InputStream inputStreamDta;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public InputStream getInputStreamDta() {
            return inputStreamDta;
        }

        public void setInputStreamDta(InputStream inputStreamDta) {
            this.inputStreamDta = inputStreamDta;
        }

        public ABNLogEntity() {
        }

        public ABNLogEntity(String dir, InputStream inputStreamDta) {
            this.dir = dir;
            this.type = 0;
            this.inputStreamDta = inputStreamDta;
        }

        public ABNLogEntity(String dir, String data) {
            this.dir = dir;
            this.type = 1;
            this.data = data;
        }
    }
}
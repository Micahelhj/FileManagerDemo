package com.admin.file.manager;

import android.app.Application;

/**
 * 项目名称：FileManagerDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/12 0012 10:26
 * 修改人：Michael-hj
 * 修改时间：2016/5/12 0012 10:26
 * 修改备注：
 */
public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ABNFileManager.initFileDir(this);
    }
}

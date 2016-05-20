package com.admin.file.manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv0 = (TextView) findViewById(R.id.btn_0);
        TextView tv1 = (TextView) findViewById(R.id.btn_1);
        TextView tv2 = (TextView) findViewById(R.id.btn_2);
        TextView tv3 = (TextView) findViewById(R.id.btn_3);
        TextView tv4 = (TextView) findViewById(R.id.btn_4);
        TextView tv5 = (TextView) findViewById(R.id.btn_5);


        File f0 = ABFileManager.getFile(ABFileManager.getCacheDownloadDir(this), "f0.txt");
        File f10 = ABFileManager.getFile(ABFileManager.getNormalLogDownloadDir(this), "f1.txt");
//        File f11 = ABFileManager.getFile(ABFileManager.getCrachLogDownloadDir(this), "f1.txt");
        File f2 = ABFileManager.getFile(ABFileManager.getDbDownloadDir(this), "f2.txt");
        File f3 = ABFileManager.getFile(ABFileManager.getFileDownloadDir(this), "f3.txt");
        File f4 = ABFileManager.getFile(ABFileManager.getImageDownloadDir(this), "f4.txt");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                ABFileManager.cleanExternalData(this);
                break;
            case R.id.btn_1:
                for (int i = 0; i < 1000; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ABLogManager.recordLog("cals","阿斯达岁的");
                }
                break;
            case R.id.btn_2:
                ABLogUtil.i("asdaaaaaaaaa");
                break;
            case R.id.btn_3:
                ABLogUtil.i("iopiopiopipi");
                ABLogManager.recordLog("cals","asdasdasdasdsad");
                ABLogManager.recordLog("cals","asdasdasdasdsad");
                ABLogManager.recordLog("cals","阿斯顿发苏打水一点");
                ABLogManager.recordLog("cals","asdasdasdasdsad");
                ABLogManager.recordLog("cals","asdasdasdasdsad");
                ABLogManager.recordLog("cals","爱上大飒飒的撒");
                ABLogManager.recordLog("asdadasd","asdasdasdasdsad");
                ABLogManager.recordLog("cals","阿斯达岁的");
                ABLogManager.recordLog("qweqwe","asdasdasdasdsad");
                ABLogManager.recordLog("cals","爱上大萨达四大四");
                ABLogManager.recordLog("123123","asdasdasdasdsad");
                break;
            default:
                break;
        }
    }
}

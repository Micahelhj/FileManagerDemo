package com.admin.file.manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.adutils.ABLogUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                ABFileManager.cleanExternalData(this);
                break;
            case R.id.btn_1:
                int a = 0;
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ABNLogManager.recordLog("cals", "阿斯达岁的" + a++);
                }
//                break;
            case R.id.btn_2:
                ABLogUtil.i("asdaaaaaaaaa");
                break;
            case R.id.btn_3:
                ABLogUtil.i("iopiopiopipi");
                ABNLogManager.recordLog("cals", "asdasdasdasdsad");
                ABNLogManager.recordLog("cals", "asdasdasdasdsad");
                ABNLogManager.recordLog("cals", "阿斯顿发苏打水一点");
                ABNLogManager.recordLog("cals", "asdasdasdasdsad");
                ABNLogManager.recordLog("cals", "asdasdasdasdsad");
                ABNLogManager.recordLog("cals", "爱上大飒飒的撒");
                ABNLogManager.recordLog("asdadasd", "asdasdasdasdsad");
                ABNLogManager.recordLog("cals", "阿斯达岁的");
                ABNLogManager.recordLog("qweqwe", "asdasdasdasdsad");
                ABNLogManager.recordLog("cals", "爱上大萨达四大四");
                ABNLogManager.recordLog("123123", "asdasdasdasdsad");
                break;
            default:
                break;
        }
    }
}

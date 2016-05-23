package com.admin.file.manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adutils.ABLogUtil;

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
                    ABLogManager.recordLog("cals", "阿斯达岁的" + a++);
                }
//                break;
            case R.id.btn_2:
                ABLogUtil.i("asdaaaaaaaaa");
                break;
            case R.id.btn_3:
                ABLogUtil.i("iopiopiopipi");
                ABLogManager.recordLog("cals", "asdasdasdasdsad");
                ABLogManager.recordLog("cals", "asdasdasdasdsad");
                ABLogManager.recordLog("cals", "阿斯顿发苏打水一点");
                ABLogManager.recordLog("cals", "asdasdasdasdsad");
                ABLogManager.recordLog("cals", "asdasdasdasdsad");
                ABLogManager.recordLog("cals", "爱上大飒飒的撒");
                ABLogManager.recordLog("asdadasd", "asdasdasdasdsad");
                ABLogManager.recordLog("cals", "阿斯达岁的");
                ABLogManager.recordLog("qweqwe", "asdasdasdasdsad");
                ABLogManager.recordLog("cals", "爱上大萨达四大四");
                ABLogManager.recordLog("123123", "asdasdasdasdsad");
                break;
            default:
                break;
        }
    }
}

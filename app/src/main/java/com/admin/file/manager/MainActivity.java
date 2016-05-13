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


        File f0 = wd.getFile(ABNFileManager.getCacheDownloadDir(this), "f0.txt");
        File f1 = wd.getFile(ABNFileManager.getCrachDownloadDir(this), "f1.txt");
        File f2 = wd.getFile(ABNFileManager.getDbDownloadDir(this), "f2.txt");
        File f3= wd.getFile(ABNFileManager.getFileDownloadDir(this), "f3.txt");
        File f4 = wd.getFile(ABNFileManager.getImageDownloadDir(this), "f4.txt");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                ABNFileManager.cleanExternalData(this);
                break;
            case R.id.btn_1:
                break;
            case R.id.btn_2:
                break;
            case R.id.btn_3:
                break;
            default:
                break;
        }
    }
}

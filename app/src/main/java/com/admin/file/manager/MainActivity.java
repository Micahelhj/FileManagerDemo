package com.admin.file.manager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.btn_0);
        long s = new File(ABFileManager.getFileDownloadDir(this)).getUsableSpace();
        try {
            textView.setText("" + ABFileManager.getFolderSize(new File(ABFileManager.getFileDownloadDir(this))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ABFileUtil.deleteFile(ABFileManager.getDbDownloadDir(this) + "/" + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
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

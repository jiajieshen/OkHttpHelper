package com.fubaisum.okhttpsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;
import com.fubaisum.okhttphelper.OkHttpRequest;
import com.fubaisum.okhttphelper.callback.OkHttpDownloadCallback;
import com.fubaisum.okhttphelper.progress.OkHttpUiProgressListener;

public class MainActivity extends AppCompatActivity {

    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTest = (TextView) findViewById(R.id.tv_test);

        new OkHttpRequest.Builder()
                .url("http://pic41.nipic.com/20140430/18021738_213628575106_2.jpg")
                .responseProgress(new OkHttpUiProgressListener() {
                    @Override
                    public void onUiProgress(long currentBytesCount, long totalBytesCount) {
                        if (totalBytesCount == -1) {
                            tvTest.setText("totalBytesCount is unknown.");
                        } else {
                            float progress = currentBytesCount * 1.0f / totalBytesCount * 100;
                            tvTest.setText("progress = " + progress);
                        }
                    }
                })
                .build()
                .requestUiCallback(new OkHttpDownloadCallback(getExternalCacheDir().getAbsolutePath(), "test.png") {
                    @Override
                    public void onResponseSuccess(String fileAbsolutePath) {
                        LogUtil.e("fileAbsolutePath = " + fileAbsolutePath);
                    }

                    @Override
                    public void onResponseError(Exception e) {
                        LogUtil.e("download onResponseError() : " + e);
                    }
                });
    }
}

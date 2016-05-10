package com.fubaisum.okhttpsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.fubaisum.okhttphelper.OkHttpRequest;
import com.fubaisum.okhttphelper.ThreadMode;
import com.fubaisum.okhttphelper.callback.DownloadCallback;
import com.fubaisum.okhttphelper.callback.ModelCallBack;
import com.fubaisum.okhttphelper.progress.UiProgressListener;
import com.scausum.okhttphelper.converter.gson.GsonConverterFactory;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTest = (TextView) findViewById(R.id.tv_test);

        testParseModel();
    }

    private void testParseModel() {
        OkHttpRequest.setConverterFactory(GsonConverterFactory.create());
        new OkHttpRequest.Builder()
                .url("http://fubaisum.github.io/testUser")
                .build()
                .threadMode(ThreadMode.MAIN)
                .callback(new ModelCallBack<User>() {

                    @Override
                    public void onResponseSuccess(User user) {
                        Log.e("MainActivity", "async success = " + user);
                    }

                    @Override
                    public void onResponseFailure(Exception e) {
                        Log.e("MainActivity", "async error = " + e);
                    }
                });

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    User user = new OkHttpRequest.Builder().url("http://fubaisum.github.io/testUser")
                            .build().model(User.class);
                    Log.e("MainActivity", "sync success = " + user);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "sync error = " + e);
                }
            }
        }.start();
    }

    private void testDownload() {
        File file = new File(getCacheDir().getAbsolutePath(), "test.png");
        new OkHttpRequest.Builder()
                .url("http://pic41.nipic.com/20140430/18021738_213628575106_2.jpg")
                .responseProgress(new UiProgressListener() {
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
                .callback(new DownloadCallback(file) {
                    @Override
                    public void onResponseSuccess(String fileAbsolutePath) {
                        Log.e("MainActivity", "fileAbsolutePath = " + fileAbsolutePath);
                    }

                    @Override
                    public void onResponseFailure(Exception e) {
                        Log.e("MainActivity", "download onResponseFailure() : " + e);
                    }
                });
    }
}

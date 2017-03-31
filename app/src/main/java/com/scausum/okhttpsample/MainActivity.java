package com.scausum.okhttpsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.scausum.okhttp.CallbackThread;
import com.scausum.okhttp.OkHttpCall;
import com.scausum.okhttp.callback.DownloadCallback;
import com.scausum.okhttp.params.FormParams;
import com.scausum.okhttp.params.MultipartParams;
import com.scausum.okhttp.progress.UiProgressListener;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String testUrl = "https://raw.githubusercontent.com/fubaisum/AndroidCollections/master/testUser.json";
    private TextView tvTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTest = (TextView) findViewById(R.id.tv_test);

        testSyncString();
        testParseModel();
        testMultipartParams();
//        testDownload();
    }

    private void testSyncString() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = new OkHttpCall.Builder()
                            .setUrl(testUrl)
                            .get()
                            .build()
                            .string();
                    Log.e("MainActivity", "sync string result = " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void testParseModel() {
        User user = new User();
        user.name = "abc";
        user.address = "unknown";
        FormParams params = new FormParams();
        params.put("user", User.class, user);
        new OkHttpCall.Builder()
                .setUrl(testUrl)
                .post(params)
                .callbackThread(CallbackThread.MAIN)//default
                .build()
                .callback(new ApiCallback<User>() {
                    @Override
                    protected void onApiSuccess(String message, User user) {
                        Log.e("MainActivity", user.toString());
                    }
                });
    }

    private void testMultipartParams() {

        String json = "{\"name\":\"5555555555555555555555\"}";
        User user = new User();
        user.name = "abc";
        user.address = "unknown";

        MultipartParams params = new MultipartParams();
        params.put("token", "fsfaiufy8jn2ir");
        params.putJson("json", json);
        params.put("user", User.class, user);
        params.putJson("userJson", User.class, user);

        new OkHttpCall.Builder()
                .setUrl(testUrl)
//                .post(params)
                .callbackThread(CallbackThread.MAIN)//default
                .build()
                .callback(new ApiCallback<User>() {

                    @Override
                    protected void onApiSuccess(String msg, User user) {
                        Log.e("MainActivity", user.toString());
                    }

                    @Override
                    protected void onApiFailure(String message) {
                        Log.e("MainActivity", "onResponseFailure : " + message);
                    }

                });
    }

    private void testDownload() {
        File file = new File(getCacheDir().getAbsolutePath(), "tmp.db");
        new OkHttpCall.Builder()
//                .setUrl("http://pic41.nipic.com/20140430/18021738_213628575106_2.jpg")
                .setUrl("http://api.youqingjia.com/db/20160614_v1.db")
                .setResponseProgressListener(new UiProgressListener() {
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
                .callbackThread(CallbackThread.BACKGROUND)
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

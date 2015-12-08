package com.fubaisum.okhttpsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fubaisum.okhttphelper.OkHttpRequest;
import com.fubaisum.okhttphelper.callback.OkHttpModelCallBack;
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
                            tvTest.setText("totalBytesCount is unknow.");
                        } else {
                            float progress = currentBytesCount * 1.0f / totalBytesCount * 100;
                            tvTest.setText("progress = " + progress);
                        }
                    }
                })
                .get()
                .uiCallback(new OkHttpModelCallBack<String>() {
                    @Override
                    public void onResponseSuccess(String result) {

                    }

                    @Override
                    public void onResponseError(Exception e) {

                    }
                });

//        new OkHttpRequest.Builder()
//                .tag(tag)
//                .url(url)
//                .appendUrlParam(key, value)
//                .appendHeader(line)
//                .appendHeader(key, value)
//                .responseProgress(progressListener)
//                .get();
//
//        InputStream inputStream = request.byteStream();
//        String string = request.string();
//        User user = request.model(User.class);
//
//        OkHttpRequest request = new OkHttpRequest.Builder()
//                .tag(tag)
//                .url(url)
//                .appendUrlParam(key, value)
//                .appendHeader(line)
//                .appendHeader(key, value)
//                .requestProgress(progressListener)
//                .responseProgress(progressListener)
//                .post();
//
//        new OkHttpDownloadCallback() {
//            @NonNull
//            @Override
//            protected String getFileName() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            protected String getDirectory() {
//                return null;
//            }
//
//            @Override
//            public void onResponseSuccess(String fileAbsolutePath) {
//
//            }
//
//            @Override
//            public void onResponseError(Exception e) {
//
//            }
//        };
//
//        request.workerCallback(new OkHttpModelCallBack<String>() {
//            @Override
//            public void onResponseSuccess(String result) {
//                //不可直接更新UI
//            }
//
//            @Override
//            public void onResponseError(Exception e) {
//                //不可直接更新UI
//            }
//        });
    }
}

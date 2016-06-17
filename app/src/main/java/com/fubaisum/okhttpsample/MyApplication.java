package com.fubaisum.okhttpsample;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fubaisum.okhttphelper.OkHttpHelper;
import com.scausum.okhttphelper.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;

/**
 * Created by sum on 5/17/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 添加model解析器
        OkHttpHelper.initConverterFactory(GsonConverterFactory.create());

        // 添加Stetho网络拦截器
        Stetho.initializeWithDefaults(this);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        OkHttpHelper.initOkHttpClient(okHttpClient);
    }
}

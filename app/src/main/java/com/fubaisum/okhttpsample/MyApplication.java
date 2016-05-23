package com.fubaisum.okhttpsample;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fubaisum.okhttphelper.OkHttpClientHolder;
import com.fubaisum.okhttphelper.OkHttpRequest;
import com.scausum.okhttphelper.converter.gson.GsonConverterFactory;

/**
 * Created by sum on 5/17/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpRequest.setConverterFactory(GsonConverterFactory.create());

        // 添加Stetho网络拦截器
        Stetho.initializeWithDefaults(this);
        OkHttpClientHolder.addNetworkInterceptor(new StethoInterceptor());
    }
}

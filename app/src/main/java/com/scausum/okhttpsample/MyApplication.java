package com.scausum.okhttpsample;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.scausum.okhttp.OkHttpManager;
import com.scausum.okhttp.converter.gson.GsonConverterFactory;

import me.drakeet.library.CrashWoodpecker;
import me.drakeet.library.PatchMode;
import okhttp3.OkHttpClient;

/**
 * Created by sum on 5/17/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashWoodpecker.instance()
                .setPatchMode(PatchMode.SHOW_LOG_PAGE)
                .setPassToOriginalDefaultHandler(true)
                .flyTo(this);

        // 添加model解析器
        OkHttpManager.setConverterFactory(GsonConverterFactory.create());

        // 添加Stetho网络拦截器
        Stetho.initializeWithDefaults(this);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addNetworkInterceptor(new ResponseInterceptor())
                .build();
        OkHttpManager.setOkHttpClient(okHttpClient);
    }
}

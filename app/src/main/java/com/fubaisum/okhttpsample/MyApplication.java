package com.fubaisum.okhttpsample;

import android.app.Application;

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
    }
}

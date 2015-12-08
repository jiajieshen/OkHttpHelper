package com.fubaisum.okhttpsample;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.fubaisum.okhttphelper.OkHttpClientHolder;

/**
 * Created by sum on 15-11-28.
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        OkHttpClientHolder.getOkHttpClient()
                .networkInterceptors()
                .add(new StethoInterceptor());
    }
}

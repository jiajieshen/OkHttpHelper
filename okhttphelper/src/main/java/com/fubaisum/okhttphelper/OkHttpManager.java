package com.fubaisum.okhttphelper;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Modifier;

/**
 * Created by sum on 15-12-3.
 */
public class OkHttpManager {

    private OkHttpClient okHttpClient;
    private Handler mainHandler;
    private Gson gson;

    private OkHttpManager() {

        okHttpClient = new OkHttpClient();

        mainHandler = new Handler(Looper.getMainLooper());

        final int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.M) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithModifiers(
                            Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC);
            gson = gsonBuilder.create();
        } else {
            gson = new Gson();
        }
    }

    private static class OkHttpRequestManagerHolder {
        private static OkHttpManager instance = new OkHttpManager();
    }

    public static OkHttpClient getOkHttpClient() {
        return OkHttpRequestManagerHolder.instance.okHttpClient;
    }

    public static Handler getMainHandler() {
        return OkHttpRequestManagerHolder.instance.mainHandler;
    }

    public static Gson getGson() {
        return OkHttpRequestManagerHolder.instance.gson;
    }

}

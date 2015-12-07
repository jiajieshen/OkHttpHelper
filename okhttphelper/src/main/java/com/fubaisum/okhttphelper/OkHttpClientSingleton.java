package com.fubaisum.okhttphelper;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by sum on 15-12-3.
 */
public class OkHttpClientSingleton {

    private static OkHttpClient okHttpClient = new OkHttpClient();

    private OkHttpClientSingleton() {
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}

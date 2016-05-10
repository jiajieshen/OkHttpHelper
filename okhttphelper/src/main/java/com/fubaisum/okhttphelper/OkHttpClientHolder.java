package com.fubaisum.okhttphelper;


import okhttp3.OkHttpClient;

/**
 * Created by sum on 15-12-3.
 */
public class OkHttpClientHolder {

    private static OkHttpClient okHttpClient = new OkHttpClient();

    private OkHttpClientHolder() {
        throw new UnsupportedOperationException("OkHttpClientHolder cannot be initialized.");
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}

package com.fubaisum.okhttphelper;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by sum on 15-12-3.
 */
public class OkHttpClientHolder {

    private static OkHttpClient okHttpClient = new OkHttpClient();

    private OkHttpClientHolder() {
        throw new UnsupportedOperationException("OkHttpClientHolder cannot be initialize.");
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}
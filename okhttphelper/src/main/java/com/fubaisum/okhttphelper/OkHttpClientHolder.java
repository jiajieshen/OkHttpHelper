package com.fubaisum.okhttphelper;


import java.util.List;

import okhttp3.Interceptor;
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

    public static void addNetworkInterceptor(Interceptor networkInterceptor) {
        okHttpClient = okHttpClient.newBuilder().addNetworkInterceptor(networkInterceptor).build();
    }

    public static void addNetworkInterceptors(List<Interceptor> networkInterceptors) {
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        for (Interceptor interceptor : networkInterceptors) {
            builder.addNetworkInterceptor(interceptor);
        }
        okHttpClient = builder.build();
    }

}

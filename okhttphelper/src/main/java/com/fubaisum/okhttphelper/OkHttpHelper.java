package com.fubaisum.okhttphelper;


import okhttp3.OkHttpClient;

/**
 * Created by sum
 */
public class OkHttpHelper {

    private static OkHttpClient okHttpClient;
    private static Converter.Factory converterFactory;

    /**
     * 静态内部类，持有默认的OkHttpClient实例
     */
    private static class OkHttpClientHolder {

        private static OkHttpClient DEFAULT_OK_HTTP_CLIENT = new OkHttpClient();
    }

    private OkHttpHelper() {
        throw new UnsupportedOperationException("OkHttpHelper cannot be initialized.");
    }


    public static void initOkHttpClient(OkHttpClient okHttpClient) {
        OkHttpHelper.okHttpClient = okHttpClient;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClientHolder.DEFAULT_OK_HTTP_CLIENT;
        }
        return okHttpClient;
    }

    /**
     * 设置ConverterFactory
     */
    public static void initConverterFactory(Converter.Factory converterFactory) {
        OkHttpHelper.converterFactory = converterFactory;
    }

    public static Converter.Factory getConverterFactory() {
        return converterFactory;
    }
}

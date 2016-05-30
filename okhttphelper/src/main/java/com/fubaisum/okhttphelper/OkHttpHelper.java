package com.fubaisum.okhttphelper;


import okhttp3.OkHttpClient;

/**
 * Created by sum on 15-12-3.
 */
public class OkHttpHelper {

    private static OkHttpClient okHttpClientSingleton;

    private OkHttpHelper() {
        throw new UnsupportedOperationException("OkHttpHelper cannot be initialized.");
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClientSingleton == null) {
            okHttpClientSingleton = OkHttpClientHolder.defaultOkHttpClient;
        }
        return okHttpClientSingleton;
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        OkHttpHelper.okHttpClientSingleton = okHttpClient;
    }

    /**
     * 静态内部类，持有默认的OkHttpClient实例
     */
    private static class OkHttpClientHolder {
        private static OkHttpClient defaultOkHttpClient = new OkHttpClient();
    }

    /**
     * 给Model解析器设置ConverterFactory
     */
    public static void setConverterFactory(Converter.Factory converterFactory) {
        ModelParser.setConverterFactory(converterFactory);
    }

}

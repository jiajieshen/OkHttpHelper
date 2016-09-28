package com.fubaisum.okhttphelper;


import okhttp3.OkHttpClient;

/**
 * Created by sum
 */
public class OkHttpHelper {

    private static OkHttpClient okHttpClient;
    private static Converter.Factory converterFactory;

    private OkHttpHelper() {
        throw new UnsupportedOperationException("OkHttpHelper cannot be initialized.");
    }

    static Platform getPlatform() {
        return Platform.get();
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        OkHttpHelper.okHttpClient = okHttpClient;
    }

    static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClientHolder.DEFAULT_OK_HTTP_CLIENT;
        }
        return okHttpClient;
    }

    /**
     * 设置ConverterFactory
     */
    public static void setConverterFactory(Converter.Factory converterFactory) {
        OkHttpHelper.converterFactory = converterFactory;
    }

    static Converter.Factory getConverterFactory() {
        if (converterFactory == null) {
            throw new NullPointerException("converterFactory == null");
        }
        return converterFactory;
    }

    /**
     * 静态内部类，持有默认的OkHttpClient实例
     */
    private static class OkHttpClientHolder {
        private static OkHttpClient DEFAULT_OK_HTTP_CLIENT = new OkHttpClient();
    }

}

package com.scausum.okhttp;


import okhttp3.OkHttpClient;

/**
 * Created by sum
 */
public class OkHttpManager {

    private static OkHttpClient okHttpClient;
    private static Converter.Factory converterFactory;

    private OkHttpManager() {
        throw new UnsupportedOperationException("OkHttpManager cannot be initialized.");
    }

    static Platform getPlatform() {
        return Platform.get();
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        OkHttpManager.okHttpClient = okHttpClient;
    }

    static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClientHolder.DEFAULT_OK_HTTP_CLIENT;
        }
        return okHttpClient;
    }

    public static void setConverterFactory(Converter.Factory converterFactory) {
        OkHttpManager.converterFactory = converterFactory;
    }

    static Converter.Factory getConverterFactory() {
        if (converterFactory == null) {
            throw new NullPointerException("converterFactory == null");
        }
        return converterFactory;
    }

    /**
     * 静态内部类，持有默认的 OkHttpClient 实例
     */
    private static class OkHttpClientHolder {
        private static OkHttpClient DEFAULT_OK_HTTP_CLIENT = new OkHttpClient();
    }

}

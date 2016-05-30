package com.fubaisum.okhttphelper;

import android.accounts.NetworkErrorException;

import com.fubaisum.okhttphelper.callback.Callback;
import com.fubaisum.okhttphelper.params.Params;
import com.fubaisum.okhttphelper.progress.ProgressHelper;
import com.fubaisum.okhttphelper.progress.ProgressListener;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by sum on 15-12-5.
 */
public class OkHttpRequest {


    private static final int METHOD_GET = 0;
    private static final int METHOD_POST = 1;

    private int requestMethod = METHOD_GET;
    private String url;
    private Headers headers;
    private ProgressListener requestProgressListener;
    private ProgressListener responseProgressListener;
    private Params params;

    private OkHttpClient okHttpClient;
    private Call call;

    private ThreadMode threadMode = ThreadMode.MAIN;

    private OkHttpRequest(String url,
                          Headers headers,
                          Params params,
                          ProgressListener requestProgressListener,
                          ProgressListener responseProgressListener) {
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.requestProgressListener = requestProgressListener;
        this.responseProgressListener = responseProgressListener;

        if (null != params) {
            this.requestMethod = METHOD_POST;
        }
    }

    public InputStream byteStream() throws Exception {
        Response response = executeSyncRequest();
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            try {
                return responseBody.byteStream();
            } finally {
                responseBody.close();
            }
        } else {
            throw new NetworkErrorException(response.toString());
        }
    }

    public String string() throws Exception {
        Response response = executeSyncRequest();
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            try {
                return responseBody.string();
            } finally {
                responseBody.close();
            }
        } else {
            throw new NetworkErrorException(response.toString());
        }
    }

    public <T> T model(Class<T> tClass) throws Exception {
        Response response = executeSyncRequest();
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            try {
                ModelParser<T> modelParser = new ModelParser<>();
                if (tClass == String.class) {
                    //noinspection unchecked
                    return (T) responseBody.string();
                } else {
                    return modelParser.parseResponse(responseBody, tClass);
                }
            } finally {
                responseBody.close();
            }
        } else {
            throw new NetworkErrorException(response.toString());
        }
    }


    public OkHttpRequest threadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
        return this;
    }

    public <T> void callback(Callback<T> callback) {
        callback.setThreadMode(threadMode);
        executeAsynRequest(callback);
    }

    private Response executeSyncRequest() throws IOException {
        Request request = buildRequest();
        okHttpClient = getOkHttpClient();
        call = okHttpClient.newCall(request);
        return call.execute();
    }

    private void executeAsynRequest(Callback callback) {
        Request request = buildRequest();
        okHttpClient = getOkHttpClient();
        call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    private Request buildRequest() {
        switch (requestMethod) {
            case METHOD_GET: {
                if (headers == null) {
                    return new Request.Builder().url(url).build();
                } else {
                    return new Request.Builder().url(url).headers(headers).build();
                }
            }
            case METHOD_POST: {
                RequestBody requestBody = params.buildRequestBody();
                if (requestProgressListener != null) {
                    requestBody = ProgressHelper.wrappedRequestProgress(
                            requestBody, requestProgressListener);
                }

                if (headers == null) {
                    return new Request.Builder().url(url).post(requestBody).build();
                } else {
                    return new Request.Builder().url(url).headers(headers).post(requestBody).build();
                }
            }
        }
        throw new IllegalStateException("The requestMethod only can be METHOD_GET or METHOD_POST.");
    }

    private OkHttpClient getOkHttpClient() {
        if (responseProgressListener == null) {
            return OkHttpHelper.getOkHttpClient();
        } else {
            OkHttpClient okHttpClient = OkHttpHelper.getOkHttpClient();
            Interceptor responseProgressInterceptor =
                    ProgressHelper.newResponseProgressInterceptor(responseProgressListener);

            return okHttpClient.newBuilder()
                    .addNetworkInterceptor(responseProgressInterceptor)
                    .build();
        }
    }

    public void cancel() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    public static class Builder {

        private String url;
        private StringBuilder urlBuilder = new StringBuilder();
        private Headers.Builder headersBuilder;
        private Params params;
        private ProgressListener requestProgressListener;
        private ProgressListener responseProgressListener;

        public Builder url(String url) {
            this.urlBuilder.append(url);
            this.urlBuilder.append("?");
            return this;
        }

        public Builder appendUrlParam(String key, String value) {
            urlBuilder.append(key).append("=").append(value).append("&");
            return this;
        }

        public Builder appendHeader(String line) {
            if (headersBuilder == null) {
                headersBuilder = new Headers.Builder();
            }
            headersBuilder.add(line);
            return this;
        }

        public Builder appendHeader(String key, String value) {
            if (headersBuilder == null) {
                headersBuilder = new Headers.Builder();
            }
            headersBuilder.add(key, value);
            return this;
        }

        public Builder post(Params params) {
            this.params = params;
            return this;
        }

        public Builder requestProgress(ProgressListener progressListener) {
            this.requestProgressListener = progressListener;
            return this;
        }

        public Builder responseProgress(ProgressListener progressListener) {
            this.responseProgressListener = progressListener;
            return this;
        }


        public OkHttpRequest build() {
            url = urlBuilder.deleteCharAt(urlBuilder.length() - 1).toString();
            Headers headers = headersBuilder != null ? headersBuilder.build() : null;
            return new OkHttpRequest(url, headers, params,
                    requestProgressListener, responseProgressListener);
        }
    }
}

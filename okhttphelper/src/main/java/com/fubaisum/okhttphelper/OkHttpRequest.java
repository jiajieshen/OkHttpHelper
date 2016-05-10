package com.fubaisum.okhttphelper;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.fubaisum.okhttphelper.callback.Callback;
import com.fubaisum.okhttphelper.params.Params;
import com.fubaisum.okhttphelper.progress.ProgressHelper;
import com.fubaisum.okhttphelper.progress.ProgressListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sum on 15-12-5.
 */
public class OkHttpRequest {

    private static final int METHOD_GET = 0;
    private static final int METHOD_POST = 1;

    @IntDef({METHOD_GET, METHOD_POST})
    private @interface RequestMethod {
    }

    @RequestMethod
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
                          Params params, ProgressListener requestProgressListener,
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
        return response.body().byteStream();
    }

    public String string() throws Exception {
        Response response = executeSyncRequest();
        return response.body().string();
    }

    public <T> T model() throws Exception {
        Response response = executeSyncRequest();
        String responseStr = response.body().string();
        Gson gson = GsonHolder.getGson();
        return gson.fromJson(responseStr, new TypeToken<T>() {
        }.getType());
    }


    public OkHttpRequest threadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
        return this;
    }

    public <T> void callback(@NonNull Callback<T> callback) {
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

    @NonNull
    private OkHttpClient getOkHttpClient() {
        if (responseProgressListener == null) {
            return OkHttpClientHolder.getOkHttpClient();
        } else {
            OkHttpClient okHttpClient = OkHttpClientHolder.getOkHttpClient();
            OkHttpClient clone = okHttpClient.newBuilder().build();
            Interceptor interceptor =
                    ProgressHelper.newResponseProgressInterceptor(responseProgressListener);
            clone.networkInterceptors().add(interceptor);
            return clone;
        }
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

    public static class Builder {

        private String url;
        private StringBuilder urlBuilder = new StringBuilder();
        private Headers headers;
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
            if (headersBuilder != null) {
                headers = headersBuilder.build();
            }
            return new OkHttpRequest(url, headers, params, requestProgressListener, responseProgressListener);
        }
    }
}

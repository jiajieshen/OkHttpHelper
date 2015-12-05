package com.fubaisum.okhttphelper;

import android.support.annotation.CheckResult;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.fubaisum.okhttphelper.callback.OkHttpCallback;
import com.fubaisum.okhttphelper.params.OkHttpParams;
import com.fubaisum.okhttphelper.progress.OkHttpProgressHelper;
import com.fubaisum.okhttphelper.progress.OkHttpProgressListener;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

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
    private Object tag;
    private String url;
    private Headers headers;
    private OkHttpProgressListener requestProgressListener;
    private OkHttpProgressListener responseProgressListener;
    private OkHttpParams params;

    private OkHttpClient okHttpClient;

    private OkHttpRequest(Object tag,
                          String url,
                          Headers headers,
                          OkHttpProgressListener requestProgressListener,
                          OkHttpProgressListener responseProgressListener) {
        this.tag = tag;
        this.url = url;
        this.headers = headers;
        this.requestProgressListener = requestProgressListener;
        this.responseProgressListener = responseProgressListener;
    }

    private void postParams(OkHttpParams params) {
        this.requestMethod = METHOD_POST;
        this.params = params;
    }

    private Response executeSyncRequest() throws IOException {
        Request request = buildRequest();
        okHttpClient = getOkHttpClient();
        return okHttpClient.newCall(request).execute();
    }

    private void executeAsynRequest(OkHttpCallback callback) {
        Request request = buildRequest();
        okHttpClient = getOkHttpClient();
        okHttpClient.newCall(request).enqueue(callback);
    }

    private Request buildRequest() {
        switch (requestMethod) {
            case METHOD_GET: {
                if (headers == null) {
                    return new Request.Builder().tag(tag).url(url).build();
                } else {
                    return new Request.Builder().tag(tag).url(url).headers(headers).build();
                }
            }
            case METHOD_POST: {
                RequestBody requestBody = params.buildRequestBody();
                if (requestProgressListener != null) {
                    requestBody =
                            OkHttpProgressHelper.wrappedRequestProgress(
                                    requestBody, requestProgressListener);
                }
                if (headers == null) {
                    return new Request.Builder().tag(tag).url(url).post(requestBody).build();
                } else {
                    return new Request.Builder().tag(tag).url(url).headers(headers).post(requestBody).build();
                }
            }
        }
        throw new IllegalStateException("The requestMethod only can be METHOD_GET or METHOD_POST.");
    }

    @NonNull
    private OkHttpClient getOkHttpClient() {
        if (responseProgressListener == null) {
            return OkHttpManager.getOkHttpClient();
        }
        OkHttpClient clone = OkHttpManager.getOkHttpClient().clone();
        Interceptor interceptor =
                OkHttpProgressHelper.newResponseProgressInterceptor(responseProgressListener);
        clone.networkInterceptors().add(interceptor);
        return clone;
    }

    public InputStream byteStream() throws IOException {
        Response response = executeSyncRequest();
        return response.body().byteStream();
    }

    public String string() throws IOException {
        Response response = executeSyncRequest();
        return response.body().string();
    }

    public <T> T model(Class<T> tClass) throws IOException {
        Response response = executeSyncRequest();
        String responseStr = response.body().string();
        if (tClass == String.class) {
            return (T) responseStr;
        } else {
            return OkHttpManager.getGson().fromJson(responseStr, tClass);
        }
    }

    public <T> void uiCallback(@NonNull OkHttpCallback<T> callback) {
        callback.setCallbackMode(OkHttpCallback.UI);
        executeAsynRequest(callback);
    }

    public <T> void workerCallback(@NonNull OkHttpCallback<T> callback) {
        callback.setCallbackMode(OkHttpCallback.WORKER);
        executeAsynRequest(callback);
    }

    public void cancel(Object tag) {
        if (okHttpClient != null) {
            okHttpClient.cancel(tag);
        }
    }

    public static class Builder {

        private Object tag;
        private String url;
        private StringBuilder urlBuilder = new StringBuilder();
        private Headers headers;
        private Headers.Builder headersBuilder;
        private OkHttpProgressListener requestProgressListener;
        private OkHttpProgressListener responseProgressListener;

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

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

        public Builder requestProgress(OkHttpProgressListener progressListener) {
            this.requestProgressListener = progressListener;
            return this;
        }

        public Builder responseProgress(OkHttpProgressListener progressListener) {
            this.responseProgressListener = progressListener;
            return this;
        }

        @CheckResult
        public OkHttpRequest get() {
            build();
            return new OkHttpRequest(tag, url, headers, requestProgressListener, responseProgressListener);
        }

        @CheckResult
        public OkHttpRequest post(OkHttpParams params) {
            build();
            OkHttpRequest request =
                    new OkHttpRequest(tag, url, headers, requestProgressListener, responseProgressListener);
            request.postParams(params);
            return request;
        }

        private void build() {
            url = urlBuilder.deleteCharAt(urlBuilder.length() - 1).toString();
            if (headersBuilder != null) {
                headers = headersBuilder.build();
            }
        }
    }
}

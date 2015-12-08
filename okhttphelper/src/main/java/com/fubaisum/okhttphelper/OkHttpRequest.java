package com.fubaisum.okhttphelper;

import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.fubaisum.okhttphelper.callback.OkHttpCallback;
import com.fubaisum.okhttphelper.params.OkHttpParams;
import com.fubaisum.okhttphelper.progress.OkHttpProgressHelper;
import com.fubaisum.okhttphelper.progress.OkHttpProgressListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;

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
                          OkHttpParams params, OkHttpProgressListener requestProgressListener,
                          OkHttpProgressListener responseProgressListener) {
        this.tag = tag;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.requestProgressListener = requestProgressListener;
        this.responseProgressListener = responseProgressListener;

        if (null != params) {
            this.requestMethod = METHOD_POST;
        }
    }

    public InputStream requestByteStream() throws IOException {
        Response response = executeSyncRequest();
        return response.body().byteStream();
    }

    public String requestString() throws IOException {
        Response response = executeSyncRequest();
        return response.body().string();
    }

    public <T> T requestModel(Class<T> tClass) throws IOException {
        Response response = executeSyncRequest();
        String responseStr = response.body().string();
        if (tClass == String.class) {
            return (T) responseStr;
        } else {
            return getGson().fromJson(responseStr, tClass);
        }
    }

    public <T> void requestUiCallback(@NonNull OkHttpCallback<T> callback) {
        callback.setCallbackMode(OkHttpCallback.UI);
        executeAsynRequest(callback);
    }

    public <T> void requestWorkerCallback(@NonNull OkHttpCallback<T> callback) {
        callback.setCallbackMode(OkHttpCallback.WORKER);
        executeAsynRequest(callback);
    }

    public void cancel() {
        if (null != okHttpClient) {
            if (null == tag) {
                throw new NullPointerException("The tag is null.");
            }
            okHttpClient.cancel(tag);
        }
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
                if (null == headers) {
                    return new Request.Builder().tag(tag).url(url).build();
                } else {
                    return new Request.Builder().tag(tag).url(url).headers(headers).build();
                }
            }
            case METHOD_POST: {
                RequestBody requestBody = params.buildRequestBody();
                if (null != requestProgressListener) {
                    requestBody =
                            OkHttpProgressHelper.wrappedRequestProgress(requestBody, requestProgressListener);
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
        if (null == responseProgressListener) {
            return OkHttpClientHolder.getOkHttpClient();
        } else {
            OkHttpClient clone = OkHttpClientHolder.getOkHttpClient().clone();
            Interceptor interceptor =
                    OkHttpProgressHelper.newResponseProgressInterceptor(responseProgressListener);
            clone.networkInterceptors().add(interceptor);
            return clone;
        }
    }

    private static Gson getGson() {
        return GsonHolder.gson;
    }

    private static class GsonHolder {
        private static final Gson gson;

        static {
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk >= Build.VERSION_CODES.M) {
                GsonBuilder gsonBuilder = new GsonBuilder()
                        .excludeFieldsWithModifiers(
                                Modifier.FINAL,
                                Modifier.TRANSIENT,
                                Modifier.STATIC);
                gson = gsonBuilder.create();
            } else {
                gson = new Gson();
            }
        }
    }

    public static class Builder {

        private Object tag;
        private String url;
        private StringBuilder urlBuilder = new StringBuilder();
        private Headers headers;
        private Headers.Builder headersBuilder;
        private OkHttpParams params;
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

        public Builder post(OkHttpParams params) {
            this.params = params;
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


        public OkHttpRequest build() {
            url = urlBuilder.deleteCharAt(urlBuilder.length() - 1).toString();
            if (headersBuilder != null) {
                headers = headersBuilder.build();
            }
            return new OkHttpRequest(tag, url, headers, params, requestProgressListener, responseProgressListener);
        }
    }
}

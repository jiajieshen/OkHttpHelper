package com.fubaisum.okhttphelper;

import android.accounts.NetworkErrorException;
import android.text.TextUtils;

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

    private static class METHOD {
        private static final String UNKNOWN = "UNKNOWN";
        private static final String GET = "GET";
        private static final String HEAD = "HEAD";
        private static final String POST = "POST";
        private static final String DELETE = "DELETE";
        private static final String PUT = "PUT";
        private static final String PATCH = "PATCH";
    }

    private Object tag;
    private String url;
    private Headers headers;
    private Params params;
    private String requestMethod = METHOD.GET;
    private ThreadMode threadMode = ThreadMode.MAIN;
    private ProgressListener requestProgressListener;
    private ProgressListener responseProgressListener;

    private OkHttpClient okHttpClient;

    private OkHttpRequest(Object tag, String url, Headers headers, String method, Params params,
                          ProgressListener requestProgressListener,
                          ProgressListener responseProgressListener) {
        this.tag = tag;
        this.url = url;
        this.headers = headers;
        this.requestMethod = method;
        this.params = params;
        this.requestProgressListener = requestProgressListener;
        this.responseProgressListener = responseProgressListener;

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
                if (tClass == String.class) {
                    //noinspection unchecked
                    return (T) responseBody.string();
                } else {
                    return ModelParser.parseResponseToModel(responseBody, tClass);
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
        callback.setPlatform(OkHttpHelper.getPlatform());
        executeAsynRequest(callback);
    }

    private Response executeSyncRequest() throws IOException {
        Request request = buildRequest();
        okHttpClient = getCurrentOkHttpClient();
        return okHttpClient.newCall(request).execute();
    }

    private void executeAsynRequest(Callback callback) {
        Request request = buildRequest();
        okHttpClient = getCurrentOkHttpClient();
        okHttpClient.newCall(request).enqueue(callback);
    }

    private Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.tag(tag).url(url);
        if (headers != null) {
            requestBuilder.headers(headers);
        }
        switch (requestMethod) {
            case METHOD.GET: {
                // do nothing
            }
            break;
            case METHOD.HEAD: {
                requestBuilder.head();
            }
            break;
            case METHOD.POST:
            case METHOD.DELETE:
            case METHOD.PUT:
            case METHOD.PATCH: {
                if (params != null) {
                    RequestBody requestBody = params.buildRequestBody();
                    if (requestProgressListener != null) {
                        requestBody = ProgressHelper.wrappedRequestProgress(requestBody, requestProgressListener);
                    }
                    requestBuilder.post(requestBody);
                }
            }
            break;
        }
        return requestBuilder.build();
    }

    private OkHttpClient getCurrentOkHttpClient() {
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
        if (tag == null) {
            throw new NullPointerException("The tag is null.");
        }
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class Builder {

        private Object tag;
        private StringBuilder urlPathBuilder = new StringBuilder();
        private StringBuilder urlQueryBuilder;
        private Headers.Builder headersBuilder;
        private ProgressListener requestProgressListener;
        private ProgressListener responseProgressListener;
        private String method = METHOD.UNKNOWN;
        private Params params;

        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder setUrl(String url) {
            urlPathBuilder.append(url);
            return this;
        }

        public Builder appendUrlPath(String path) {
            urlPathBuilder.append("/");
            urlPathBuilder.append(path);
            return this;
        }

        public Builder addUrlQuery(String key, String value) {
            if (urlQueryBuilder == null) {
                urlQueryBuilder = new StringBuilder();
            }
            urlQueryBuilder.append(key).append("=").append(value).append("&");
            return this;
        }

        public Builder addHeader(String line) {
            if (headersBuilder == null) {
                headersBuilder = new Headers.Builder();
            }
            headersBuilder.add(line);
            return this;
        }

        public Builder addHeader(String key, String value) {
            if (headersBuilder == null) {
                headersBuilder = new Headers.Builder();
            }
            headersBuilder.add(key, value);
            return this;
        }

        public Builder setRequestProgressListener(ProgressListener progressListener) {
            requestProgressListener = progressListener;
            return this;
        }

        public Builder setResponseProgressListener(ProgressListener progressListener) {
            responseProgressListener = progressListener;
            return this;
        }

        public Builder get() {
            method = METHOD.GET;
            return this;
        }

        public Builder post(Params p) {
            method = METHOD.POST;
            params = p;
            return this;
        }

        public Builder head() {
            method = METHOD.HEAD;
            return this;
        }

        public Builder delete(Params p) {
            method = METHOD.DELETE;
            params = p;
            return this;
        }

        public Builder put(Params p) {
            method = METHOD.PUT;
            params = p;
            return this;
        }

        public Builder patch(Params p) {
            method = METHOD.PATCH;
            params = p;
            return this;
        }

        public OkHttpRequest build() {

            if (METHOD.UNKNOWN.equals(method)) {
                throw new IllegalArgumentException("The method is unknown.");
            }

            if (urlQueryBuilder != null) {
                urlQueryBuilder.deleteCharAt(urlQueryBuilder.length() - 1);

                urlPathBuilder.append("?");
                urlPathBuilder.append(urlQueryBuilder.toString());
            }
            String url = urlPathBuilder.toString();
            if (TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("The setUrl is null.");
            }

            Headers headers = headersBuilder != null ? headersBuilder.build() : null;

            return new OkHttpRequest(tag, url, headers, method, params,
                    requestProgressListener, responseProgressListener);
        }
    }
}

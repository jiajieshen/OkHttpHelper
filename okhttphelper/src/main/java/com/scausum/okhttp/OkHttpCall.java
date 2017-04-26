package com.scausum.okhttp;

import android.accounts.NetworkErrorException;
import android.text.TextUtils;

import com.scausum.okhttp.callback.Callback;
import com.scausum.okhttp.params.Params;
import com.scausum.okhttp.progress.ProgressHelper;
import com.scausum.okhttp.progress.ProgressListener;
import com.scausum.okhttp.rx.ModelObservableOnSubscribe;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
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
public class OkHttpCall {

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
    private String requestMethod;
    private String url;
    private Headers headers;
    private Params params;
    private CallbackThread callbackThread;
    private ProgressListener requestProgressListener;
    private ProgressListener responseProgressListener;

    private OkHttpClient okHttpClient;

    private OkHttpCall(Object tag, String method, String url, Headers headers, Params params, CallbackThread callbackThread,
                       ProgressListener requestProgressListener, ProgressListener responseProgressListener) {
        this.tag = tag;
        this.requestMethod = method;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.callbackThread = callbackThread;
        this.requestProgressListener = requestProgressListener;
        this.responseProgressListener = responseProgressListener;
    }

    public InputStream byteStream() throws Exception {
        Response response = execute();
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
        Response response = execute();
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
        Response response = execute();
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

    public <T> Observable<T> toObservable(ModelObservableOnSubscribe<T> modelObservableOnSubscribe) {
        Request request = buildRequest();
        okHttpClient = getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        modelObservableOnSubscribe.setCall(call);

        return Observable.create(modelObservableOnSubscribe);
    }



    public <T> void callback(Callback<T> callback) {
        callback.setCallbackThread(callbackThread);
        callback.setPlatform(OkHttpManager.getPlatform());
        enqueue(callback);
    }

    private Response execute() throws IOException {
        Request request = buildRequest();
        okHttpClient = getOkHttpClient();
        return okHttpClient.newCall(request).execute();
    }

    private void enqueue(Callback callback) {
        Request request = buildRequest();
        okHttpClient = getOkHttpClient();
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

    private OkHttpClient getOkHttpClient() {
        if (responseProgressListener == null) {
            return OkHttpManager.getOkHttpClient();
        } else {
            OkHttpClient okHttpClient = OkHttpManager.getOkHttpClient();
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
        private CallbackThread callbackThread = CallbackThread.MAIN;
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

        public Builder callbackThread(CallbackThread callbackThread) {
            this.callbackThread = callbackThread;
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

        public OkHttpCall build() {
            if (METHOD.UNKNOWN.equals(method)) {
                throw new IllegalArgumentException("The request method is unknown.");
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

            return new OkHttpCall(tag, method, url, headers, params, callbackThread,
                    requestProgressListener, responseProgressListener);
        }
    }
}

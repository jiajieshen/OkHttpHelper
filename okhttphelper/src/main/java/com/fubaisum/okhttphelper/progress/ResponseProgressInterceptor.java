package com.fubaisum.okhttphelper.progress;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by sum on 15-12-2.
 */
class ResponseProgressInterceptor implements Interceptor {

    private OkHttpProgressListener progressListener;

    public ResponseProgressInterceptor(OkHttpProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        ProgressResponseBody progressResponseBody =
                new ProgressResponseBody(originalResponse.body(), progressListener);
        return originalResponse.newBuilder()
                .body(progressResponseBody)
                .build();
    }
}

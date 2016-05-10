package com.fubaisum.okhttphelper.progress;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by sum on 15-12-2.
 */
class ResponseProgressInterceptor implements Interceptor {

    private ProgressListener progressListener;

    public ResponseProgressInterceptor(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        ProgressResponseBody progressResponseBody =
                new ProgressResponseBody(originalResponse.body(), progressListener);
        return originalResponse.newBuilder().body(progressResponseBody).build();
    }
}

package com.fubaisum.okhttphelper.progress;


import okhttp3.Interceptor;
import okhttp3.RequestBody;

/**
 * Created by sum on 15-12-2.
 */
public class ProgressHelper {

    public static RequestBody wrappedRequestProgress(
            RequestBody requestBody, ProgressListener progressListener) {
        return new ProgressRequestBody(requestBody, progressListener);
    }

    public static Interceptor newResponseProgressInterceptor(ProgressListener progressListener) {
        return new ResponseProgressInterceptor(progressListener);
    }
}

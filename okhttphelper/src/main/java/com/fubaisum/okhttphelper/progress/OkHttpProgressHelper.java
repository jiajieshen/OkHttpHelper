package com.fubaisum.okhttphelper.progress;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.RequestBody;

/**
 * Created by sum on 15-12-2.
 */
public class OkHttpProgressHelper {

    public static RequestBody wrappedRequestProgress(
            RequestBody requestBody, OkHttpProgressListener progressListener) {
        return new ProgressRequestBody(requestBody, progressListener);
    }

    /**
     * <code>
     * Interceptor interceptor = newResponseProgressInterceptor(okHttpProgressListener);
     * okHttpClient.networkInterceptors().add(interceptor);
     * </code>
     */
    public static Interceptor newResponseProgressInterceptor(OkHttpProgressListener progressListener) {
        return new ResponseProgressInterceptor(progressListener);
    }
}

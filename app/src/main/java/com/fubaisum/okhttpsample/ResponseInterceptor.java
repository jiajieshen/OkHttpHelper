package com.fubaisum.okhttpsample;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by sum on 9/28/16.
 */

public class ResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Log.e("ResponseInterceptor", "intercept response = " + response);
        return response;
    }
}

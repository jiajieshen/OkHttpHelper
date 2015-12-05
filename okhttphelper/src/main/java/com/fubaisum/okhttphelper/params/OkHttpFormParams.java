package com.fubaisum.okhttphelper.params;

import android.support.annotation.NonNull;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

/**
 * Created by sum on 15-9-29.
 */
public class OkHttpFormParams implements OkHttpParams {

    private FormEncodingBuilder formEncodingBuilder;

    public OkHttpFormParams() {
        formEncodingBuilder = new FormEncodingBuilder();
    }

    public void put(String key, String value) {
        formEncodingBuilder.add(key, value);
    }

    @NonNull
    public RequestBody buildRequestBody() {
        return formEncodingBuilder.build();
    }
}

package com.fubaisum.okhttphelper.params;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

/**
 * Created by sum on 15-12-4.
 */
public class OkHttpJsonParams implements OkHttpParams {

    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private String json;

    public OkHttpJsonParams(String json) {
        this.json = json;
    }

    @Override
    public RequestBody buildRequestBody() {
        return RequestBody.create(MEDIA_TYPE_JSON, json);
    }
}

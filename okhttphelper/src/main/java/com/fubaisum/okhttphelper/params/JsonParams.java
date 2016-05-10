package com.fubaisum.okhttphelper.params;


import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by sum on 15-12-4.
 */
public class JsonParams implements Params {

    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private String json;

    public JsonParams(String json) {
        this.json = json;
    }

    @Override
    public RequestBody buildRequestBody() {
        return RequestBody.create(MEDIA_TYPE_JSON, json);
    }
}

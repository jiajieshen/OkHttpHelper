package com.scausum.okhttp.params;


import com.scausum.okhttp.ModelParser;

import java.io.IOException;

import okhttp3.RequestBody;

/**
 * Created by sum on 15-12-4.
 */
public class JsonParams implements Params {

    private String json;
    private RequestBody requestBody;

    public JsonParams(String json) {
        this.json = json;
    }

    public <T> JsonParams(Class<T> tClass, T t) {
        try {
            requestBody = ModelParser.parseModelToRequestBody(tClass, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RequestBody buildRequestBody() {
        if (json != null) {
            return RequestBody.create(MEDIA_TYPE_JSON, json);
        } else if (requestBody != null) {
            return requestBody;
        } else {
            throw new RuntimeException();
        }
    }
}

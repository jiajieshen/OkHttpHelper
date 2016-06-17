package com.fubaisum.okhttphelper.params;


import com.fubaisum.okhttphelper.ModelParser;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by sum on 15-12-4.
 */
public class JsonParams implements Params {

    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
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

package com.fubaisum.okhttphelper.params;


import com.fubaisum.okhttphelper.Converter;
import com.fubaisum.okhttphelper.OkHttpHelper;

import java.io.IOException;

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

    public <T> JsonParams(Class<T> tClass, T t) {
        Converter.Factory converterFactory = OkHttpHelper.getConverterFactory();
        if (converterFactory == null) {
            throw new NullPointerException(
                    "Please invoke OkHttpHelper.initConverterFactory() method first.");
        }
        //noinspection unchecked
        Converter<T,RequestBody> requestBodyConverter =
                (Converter<T, RequestBody>) converterFactory.requestBodyConverter(tClass);
        try {
            RequestBody requestBody = requestBodyConverter.convert(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RequestBody buildRequestBody() {
        return RequestBody.create(MEDIA_TYPE_JSON, json);
    }
}

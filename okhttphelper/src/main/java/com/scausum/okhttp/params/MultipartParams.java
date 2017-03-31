package com.scausum.okhttp.params;

import com.scausum.okhttp.ModelParser;

import java.io.File;
import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by sum on 15-10-7.
 */
public class MultipartParams implements Params {

    private MultipartBody.Builder multipartBuilder;

    public MultipartParams() {
        multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    }

    public MultipartParams put(String key, String value) {
        multipartBuilder.addFormDataPart(key, value);
        return this;
    }

    public <T> MultipartParams put(String key, Class<T> tClass, T t) {
        try {
            String value = ModelParser.parseModelToString(tClass, t);
            multipartBuilder.addFormDataPart(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public MultipartParams put(String key, File file) {
        String fileName = file.getName();
        RequestBody fileRequestBody = RequestBody.create(MEDIA_TYPE_STREAM, file);
        multipartBuilder.addFormDataPart(key, fileName, fileRequestBody);
        return this;
    }

    public MultipartParams put(String key, String fileName, byte[] bytes) {
        RequestBody fileRequestBody = RequestBody.create(MEDIA_TYPE_STREAM, bytes);
        multipartBuilder.addFormDataPart(key, fileName, fileRequestBody);
        return this;
    }

    public MultipartParams putJson(String key, String json) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        multipartBuilder.addPart(
                Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                requestBody);
        return this;
    }

    public <T> MultipartParams putJson(String key, Class<T> tClass, T t) {
        try {
            RequestBody requestBody = ModelParser.parseModelToRequestBody(tClass, t);
            multipartBuilder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                    requestBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public RequestBody buildRequestBody() {
        return multipartBuilder.build();
    }
}

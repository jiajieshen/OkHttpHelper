package com.fubaisum.okhttphelper.params;

import java.io.File;

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
        multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""), requestBody);
        return this;
    }

    @Override
    public RequestBody buildRequestBody() {
        return multipartBuilder.build();
    }
}

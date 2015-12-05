package com.fubaisum.okhttphelper.params;

import android.support.annotation.NonNull;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;

/**
 * Created by sum on 15-10-7.
 */
public class OkHttpMultiTypeParams implements OkHttpParams {

    private final MediaType MEDIA_TYPE_STREAM =
            MediaType.parse("application/octet-stream;charset=utf-8");

    private MultipartBuilder multipartBuilder;

    public OkHttpMultiTypeParams() {
        multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
    }

    public void put(String key, String value) {
        multipartBuilder.addPart(
                Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                RequestBody.create(null, value));
    }

    public void put(String key, File file) {
        RequestBody fileRequestBody = RequestBody.create(MEDIA_TYPE_STREAM, file);
        String fileName = file.getName();
        multipartBuilder.addPart(
                Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                fileRequestBody);
    }

    public void put(String key, String fileName, byte[] bytes) {
        RequestBody fileRequestBody = RequestBody.create(MEDIA_TYPE_STREAM, bytes);
        multipartBuilder.addPart(
                Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                fileRequestBody);
    }

    @NonNull
    @Override
    public RequestBody buildRequestBody() {
        return multipartBuilder.build();
    }
}

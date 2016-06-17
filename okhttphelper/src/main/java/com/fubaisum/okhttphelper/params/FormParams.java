package com.fubaisum.okhttphelper.params;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by sum on 15-9-29.
 */
public class FormParams implements Params {

    private FormBody.Builder formBodyBuilder;

    public FormParams() {
        formBodyBuilder = new FormBody.Builder();
    }

    public FormParams put(String key, String value) {
        formBodyBuilder.add(key, value);
        return this;
    }

    public RequestBody buildRequestBody() {
        return formBodyBuilder.build();
    }
}

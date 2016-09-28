package com.fubaisum.okhttphelper.params;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by sum on 15-10-2.
 */
public interface Params {

    MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain; charset=utf-8");
    MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream; charset=utf-8");
    MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    RequestBody buildRequestBody();
}

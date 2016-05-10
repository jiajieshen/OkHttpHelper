package com.fubaisum.okhttphelper;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by sum on 5/10/16.
 */
public interface Converter<F, T> {
    T convert(F value) throws IOException;

    abstract class Factory {

        public Converter<ResponseBody, ?> responseBodyConverter(Type type) {
            return null;
        }

        public Converter<?, RequestBody> requestBodyConverter(Type type) {
            return null;
        }

        public Converter<?, String> stringConverter(Type type) {
            return null;
        }
    }
}

package com.fubaisum.okhttphelper;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by sum on 5/10/16.
 */
public class ModelParser {

    private ModelParser() {
        throw new UnsupportedOperationException();
    }

    public static <T> T parseResponseToModel(ResponseBody responseBody, Type modelType) throws Exception {
        Converter.Factory converterFactory = OkHttpHelper.getConverterFactory();
        if (converterFactory == null) {
            throw new NullPointerException("Please invoke OkHttpHelper.initConverterFactory() method first.");
        }
        //noinspection unchecked
        Converter<ResponseBody, T> responseConverter
                = (Converter<ResponseBody, T>) converterFactory.responseBodyConverter(modelType);
        return responseConverter.convert(responseBody);
    }

    public static <T> RequestBody parseModelToRequestBody(Type modelType, T value) throws IOException {
        Converter.Factory converterFactory = OkHttpHelper.getConverterFactory();
        if (converterFactory == null) {
            throw new NullPointerException("Please invoke OkHttpHelper.initConverterFactory() method first.");
        }
        //noinspection unchecked
        Converter<T, RequestBody> requestBodyConverter =
                (Converter<T, RequestBody>) converterFactory.requestBodyConverter(modelType);
        return requestBodyConverter.convert(value);
    }
}

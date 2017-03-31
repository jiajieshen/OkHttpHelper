package com.scausum.okhttp;

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
        Converter.Factory converterFactory = getConverterFactory();
        //noinspection unchecked
        Converter<ResponseBody, T> responseConverter = (Converter<ResponseBody, T>)
                converterFactory.responseBodyConverter(modelType);
        return responseConverter.convert(responseBody);
    }

    public static <T> RequestBody parseModelToRequestBody(Type modelType, T value) throws IOException {
        Converter.Factory converterFactory = OkHttpManager.getConverterFactory();
        if (converterFactory == null) {
            throw new NullPointerException("Please invoke OkHttpManager.initConverterFactory() method first.");
        }
        //noinspection unchecked
        Converter<T, RequestBody> requestBodyConverter = (Converter<T, RequestBody>)
                converterFactory.requestBodyConverter(modelType);
        return requestBodyConverter.convert(value);
    }

    public static <T> String parseModelToString(Type modelType, T value) throws Exception {
        Converter.Factory converterFactory = getConverterFactory();
        //noinspection unchecked
        Converter<T, String> stringConverter = (Converter<T, String>)
                converterFactory.stringConverter(modelType);
        if (stringConverter == null) {
            throw new NullPointerException("ConverterFactory.stringConverter() return null.");
        }
        return stringConverter.convert(value);
    }

    private static Converter.Factory getConverterFactory() {
        Converter.Factory converterFactory = OkHttpManager.getConverterFactory();
        if (converterFactory == null) {
            throw new NullPointerException("OkHttpManager.getConverterFactory() return null.");
        }
        return converterFactory;
    }
}

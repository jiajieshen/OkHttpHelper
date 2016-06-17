package com.fubaisum.okhttphelper;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Created by sum on 5/10/16.
 */
public class ModelParser<T> {

    protected static Converter.Factory converterFactory;

    static void setConverterFactory(Converter.Factory converterFactory) {
        ModelParser.converterFactory = converterFactory;
    }

    public T parseResponse(ResponseBody responseBody, Type modelType) throws Exception {
        if (converterFactory == null) {
            throw new NullPointerException("The converterFactory can't be null.");
        }
        //noinspection unchecked
        Converter<ResponseBody, T> responseConverter
                = (Converter<ResponseBody, T>) converterFactory.responseBodyConverter(modelType);
        return responseConverter.convert(responseBody);
    }
}

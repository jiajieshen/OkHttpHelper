package com.fubaisum.okhttphelper.callback;

import com.fubaisum.okhttphelper.OkHttpManager;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by sum on 15-12-3.
 */
public abstract class OkHttpModelCallBack<T> extends OkHttpCallback<T> {

    private Type modelType;

    public OkHttpModelCallBack() {
        modelType = getSuperclassTypeParameter(getClass());
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    @Override
    public void onFailure(Request request, IOException e) {
        sendFailureCallback(e);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            sendFailureCallback(new RuntimeException(response.toString()));
            return;
        }
        String responseStr = response.body().string();
        if (modelType == String.class) {
            sendSuccessCallback((T) responseStr);
        } else {
            T result = OkHttpManager.getGson().fromJson(responseStr, modelType);
            sendSuccessCallback(result);
        }
    }

}

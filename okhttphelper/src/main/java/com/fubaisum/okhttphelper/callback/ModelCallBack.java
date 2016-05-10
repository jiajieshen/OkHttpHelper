package com.fubaisum.okhttphelper.callback;

import android.accounts.NetworkErrorException;

import com.fubaisum.okhttphelper.GsonHolder;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sum on 15-12-3.
 */
public abstract class ModelCallBack<T> extends Callback<T> {

    private Type modelType;

    public ModelCallBack() {
        modelType = getSuperclassTypeParameter(getClass());
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        sendFailureCallback(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            sendFailureCallback(new NetworkErrorException(response.toString()));
        } else {
            try {
                String responseStr = response.body().string();
                if (modelType == String.class) {
                    sendSuccessCallback((T) responseStr);
                } else {
                    Gson gson = GsonHolder.getGson();
                    T result = gson.fromJson(responseStr, modelType);
                    sendSuccessCallback(result);
                }
            } catch (Exception e) {
                sendFailureCallback(e);
            }finally {
                response.body().close();
            }
        }
    }
}

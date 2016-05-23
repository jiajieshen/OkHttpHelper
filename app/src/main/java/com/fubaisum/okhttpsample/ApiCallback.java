package com.fubaisum.okhttpsample;

import android.util.Log;

import com.fubaisum.okhttphelper.callback.ModelCallBack;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by sum on 5/17/16.
 */
public abstract class ApiCallback<T> extends ModelCallBack<Api<T>> {

    private static final String NETWORK_ERROR = "网络错误";

    public ApiCallback() {
        Type modelType = getGenericTypeParameter(getClass().getSuperclass());
        Log.e("ApiCallback", "modelType = " + modelType);
    }

    protected static Type getGenericTypeParameter(Class<?> thisClass) {
        Type superclass = thisClass.getGenericSuperclass();
        Log.e("ApiCallback", "superclass = " + superclass);
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing modelType parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        Log.e("ApiCallback", "parameterizedType = " + parameterizedType);
        return parameterizedType.getActualTypeArguments()[0];
    }



    protected abstract void onApiSuccess(T data);

    protected abstract void onApiFailure(String message);

    @Override
    public void onResponseSuccess(Api<T> result) {
        if (result.status) {
            onApiSuccess(result.data);
        } else {
            onApiFailure(result.message);
        }
    }

    @Override
    public void onResponseFailure(Exception e) {
        Log.e("ApiCallback", "Network error = " + e);
        onApiFailure(NETWORK_ERROR);
    }
}

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

    @Override
    protected Type genericModelType() {
        final Type subType = getGenericTypeParameter(getClass());
        return new ParameterizedType() {

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{subType};
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return Api.class;
            }
        };
    }

    protected abstract void onApiSuccess(String message, T data);

    protected void onApiFailure(String message) {
    }

    @Override
    public final void onResponseSuccess(Api<T> result) {
        if (result.status) {
            onApiSuccess(result.msg, result.data);
        } else {
            onApiFailure(result.msg);
        }
    }

    @Override
    public final void onResponseFailure(Exception e) {
        Log.e("ApiCallback", "Network error = " + e);
        onApiFailure(NETWORK_ERROR);
    }
}

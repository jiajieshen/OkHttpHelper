package com.fubaisum.okhttpsample;

import android.util.Log;

import com.fubaisum.okhttphelper.callback.ModelCallBack;

/**
 * Created by sum on 5/17/16.
 */
public abstract class AppModelCallback<T> extends ModelCallBack<NetResult<T>> {

    @Override
    public void onResponseSuccess(NetResult<T> result) {
        if (result.status) {
            onSuccess(result.data);
        } else {
            onFailure(result.message);
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(String errorMessage);

    @Override
    public void onResponseFailure(Exception e) {
        Log.e("AppModelCallback", "error = " + e);
    }
}

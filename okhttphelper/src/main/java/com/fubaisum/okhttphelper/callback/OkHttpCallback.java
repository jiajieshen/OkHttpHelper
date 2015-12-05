package com.fubaisum.okhttphelper.callback;

import android.support.annotation.IntDef;

import com.fubaisum.okhttphelper.OkHttpManager;
import com.squareup.okhttp.Callback;

/**
 * Created by sum on 15-12-4.
 */
public abstract class OkHttpCallback<T> implements Callback {

    public static final int UI = 0;
    public static final int WORKER = 1;

    @IntDef({UI, WORKER})
    @interface CallbackMode {
    }

    @CallbackMode
    private int callbackMode = UI;

    public void setCallbackMode(@CallbackMode int callbackMode) {
        this.callbackMode = callbackMode;
    }

    protected void sendFailureCallback(final Exception e) {
        switch (callbackMode) {
            case UI: {
                OkHttpManager.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onResponseError(e);
                    }
                });
                break;
            }
            case WORKER: {
                onResponseError(e);
                break;
            }
        }
    }

    protected void sendSuccessCallback(final T result) {
        switch (callbackMode) {
            case UI: {
                OkHttpManager.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onResponseSuccess(result);
                    }
                });
                break;
            }
            case WORKER: {
                onResponseSuccess(result);
                break;
            }
        }
    }

    public abstract void onResponseSuccess(T result);

    public abstract void onResponseError(Exception e);

}

package com.fubaisum.okhttphelper.callback;


import android.os.Handler;
import android.os.Looper;

import com.fubaisum.okhttphelper.ThreadMode;

/**
 * Created by sum on 5/9/16.
 */
public abstract class Callback<T> implements okhttp3.Callback {

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    private ThreadMode threadMode = ThreadMode.MAIN;

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    protected final void sendFailureCallback(final Exception e) {
        switch (threadMode) {
            case MAIN: {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResponseFailure(e);
                    }
                });
                break;
            }
            case BACKGROUND: {
                onResponseFailure(e);
                break;
            }
        }
    }

    protected final void sendSuccessCallback(final T result) {
        switch (threadMode) {
            case MAIN: {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResponseSuccess(result);
                    }
                });
                break;
            }
            case BACKGROUND: {
                onResponseSuccess(result);
                break;
            }
        }
    }

    protected abstract void onResponseSuccess(T result);

    protected abstract void onResponseFailure(Exception e);

}

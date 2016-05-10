package com.fubaisum.okhttphelper.callback;


import android.os.Handler;
import android.os.Looper;

import com.fubaisum.okhttphelper.ThreadMode;

/**
 * Created by sum on 5/9/16.
 */
public abstract class Callback<T> implements okhttp3.Callback{

    private ThreadMode threadMode = ThreadMode.MAIN;

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    protected void sendFailureCallback(final Exception e) {
        switch (threadMode) {
            case MAIN: {
                getMainHandler().post(new Runnable() {
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

    protected void sendSuccessCallback(final T result) {
        switch (threadMode) {
            case MAIN: {
                getMainHandler().post(new Runnable() {
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

    public abstract void onResponseSuccess(T result);

    public abstract void onResponseFailure(Exception e);

    /**
     *
     */
    protected static Handler getMainHandler() {
        return MainHandlerHolder.mainHandlerInstance;
    }

    private static class MainHandlerHolder {
        private static final Handler mainHandlerInstance = new Handler(Looper.getMainLooper());
    }
}

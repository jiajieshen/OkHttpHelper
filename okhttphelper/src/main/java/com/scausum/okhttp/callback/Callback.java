package com.scausum.okhttp.callback;


import com.scausum.okhttp.CallbackThread;
import com.scausum.okhttp.Platform;

/**
 * Created by sum on 5/9/16.
 */
public abstract class Callback<T> implements okhttp3.Callback {


    private CallbackThread callbackThread = CallbackThread.MAIN;
    private Platform platform;

    public void setCallbackThread(CallbackThread callbackThread) {
        this.callbackThread = callbackThread;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    protected final void sendFailureCallback(final Exception e) {
        switch (callbackThread) {
            case MAIN: {
                if (platform == null) {
                    throw new NullPointerException("The platform is null.");
                }
                platform.execute(new Runnable() {
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
        switch (callbackThread) {
            case MAIN: {
                if (platform == null) {
                    throw new NullPointerException("The platform is null.");
                }
                platform.execute(new Runnable() {
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

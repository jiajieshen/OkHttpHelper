package com.fubaisum.okhttphelper.callback;


import com.fubaisum.okhttphelper.Platform;
import com.fubaisum.okhttphelper.ThreadMode;

/**
 * Created by sum on 5/9/16.
 */
public abstract class Callback<T> implements okhttp3.Callback {


    private ThreadMode threadMode = ThreadMode.MAIN;
    private Platform platform;

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    protected final void sendFailureCallback(final Exception e) {
        switch (threadMode) {
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
        switch (threadMode) {
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

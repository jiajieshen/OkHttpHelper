package com.fubaisum.okhttphelper.progress;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by sum on 15-12-8.
 */
public abstract class OkHttpUiProgressListener implements OkHttpProgressListener {

    private static final int MSG_UI_PROGRESS = 0x123;
    private static final long DEFAULT_PROGRESS_PERIOD = 600;

    private final UiHandler uiHandler = new UiHandler(this);
    private long currentBytesCount;
    private long totalBytesCount;
    private boolean isFirst = true;
    private long startTimeMillis;
    private long progressPeriod;

    @Override
    public final void onProgress(long currentBytesCount, long totalBytesCount) {
        if (isFirst) {
            isFirst = false;
            startTimeMillis = System.currentTimeMillis();
            progressPeriod = getUiProgressPeriod();
        } else if (currentBytesCount == totalBytesCount) {
            /**
             * 请求/响应结束时，必须回调进度
             */
            this.currentBytesCount = currentBytesCount;
            this.totalBytesCount = totalBytesCount;
            uiHandler.sendEmptyMessage(MSG_UI_PROGRESS);
        } else {
            /**
             * 请求/响应过程中，至少延迟指定的周期，才回调进度
             */
            long crrTimeMillis = System.currentTimeMillis();
            if (crrTimeMillis - startTimeMillis >= progressPeriod) {
                startTimeMillis = crrTimeMillis;
                this.currentBytesCount = currentBytesCount;
                this.totalBytesCount = totalBytesCount;
                uiHandler.sendEmptyMessage(MSG_UI_PROGRESS);
            }
        }
    }

    protected long getUiProgressPeriod() {
        return DEFAULT_PROGRESS_PERIOD;
    }

    protected abstract void onUiProgress(long currentBytesCount, long totalBytesCount);

    private static class UiHandler extends Handler {

        private WeakReference<OkHttpUiProgressListener> weakReference;

        private UiHandler(OkHttpUiProgressListener uiProgressListener) {
            super(Looper.getMainLooper());
            weakReference = new WeakReference<>(uiProgressListener);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MSG_UI_PROGRESS) {
                return;
            }
            OkHttpUiProgressListener uiProgressListener = weakReference.get();
            if (null != uiProgressListener) {
                uiProgressListener.onUiProgress(
                        uiProgressListener.currentBytesCount, uiProgressListener.totalBytesCount);
            }
        }
    }
}

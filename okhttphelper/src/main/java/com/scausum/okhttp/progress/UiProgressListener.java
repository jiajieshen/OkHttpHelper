package com.scausum.okhttp.progress;

import com.scausum.okhttp.Platform;

import java.lang.ref.WeakReference;

/**
 * Created by sum on 15-12-8.
 */
public abstract class UiProgressListener implements ProgressListener {

    private static final long DEFAULT_PROGRESS_PERIOD = 600;

    private long progressPeriod;
    private Platform platform;
    private UiRunnable uiRunnable;

    private boolean isFirst = true;
    private long startTimeMillis;

    private long currentBytesCount;
    private long totalBytesCount;

    public UiProgressListener() {
        this(0);
    }

    public UiProgressListener(long progressPeriod) {
        this.progressPeriod = progressPeriod > 0 ? progressPeriod : DEFAULT_PROGRESS_PERIOD;
        platform = Platform.get();
        uiRunnable = new UiRunnable(this);
    }

    @Override
    public final void onProgress(long currentBytesCount, long totalBytesCount) {
        if (isFirst) {
            isFirst = false;
            startTimeMillis = System.currentTimeMillis();
        } else if (currentBytesCount == totalBytesCount) {
            /**
             * 请求/响应结束时，必须回调进度
             */
            this.currentBytesCount = currentBytesCount;
            this.totalBytesCount = totalBytesCount;
            platform.execute(uiRunnable);
        } else {
            /**
             * 请求/响应过程中，至少延迟指定的周期，才回调进度
             */
            long crrTimeMillis = System.currentTimeMillis();
            if (crrTimeMillis - startTimeMillis >= progressPeriod) {
                startTimeMillis = crrTimeMillis;
                this.currentBytesCount = currentBytesCount;
                this.totalBytesCount = totalBytesCount;
                platform.execute(uiRunnable);
            }
        }
    }

    protected abstract void onUiProgress(long currentBytesCount, long totalBytesCount);

    private static class UiRunnable implements Runnable {

        private WeakReference<UiProgressListener> reference;

        UiRunnable(UiProgressListener uiProgressListener) {
            reference = new WeakReference<>(uiProgressListener);
        }

        @Override
        public void run() {
            UiProgressListener uiProgressListener = reference.get();
            if (uiProgressListener != null) {
                uiProgressListener.onUiProgress(
                        uiProgressListener.currentBytesCount, uiProgressListener.totalBytesCount);
            }
        }
    }

}

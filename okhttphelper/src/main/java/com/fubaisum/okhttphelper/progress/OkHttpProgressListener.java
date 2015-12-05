package com.fubaisum.okhttphelper.progress;

/**
 * Created by sum on 15-12-2.
 */
public interface OkHttpProgressListener {
    /**
     * @param currentBytesCount
     * @param totalBytesCount   未知大小时，值为-1
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}

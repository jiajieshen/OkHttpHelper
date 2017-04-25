package com.scausum.okhttp;

import okhttp3.Call;

public interface CallAdapter<T> {

    /** call执行的代理方法 */
    <R> T adapt(Call call);
}
package com.scausum.okhttp.callback;

/**
 * Created by xin on 4/25/17.
 */

public class ModelType<T> {

    public static <T> ModelType<T> create() {
        return new ModelType<>();
    }
}

package com.scausum.okhttphelper.converter.gson;

import com.google.gson.Gson;

/**
 * Created by sum on 5/10/16.
 */
public class GsonHolder {

    private volatile static Gson gson;

    private GsonHolder() {
        throw new UnsupportedOperationException();
    }

    public static Gson getGson() {
        if (gson == null) {
            synchronized (GsonHolder.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}

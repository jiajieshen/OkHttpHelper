package com.fubaisum.okhttphelper;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

/**
 * Created by sum on 2/18/16.
 */
public class GsonHolder {

    private static final Gson gson;

    static {
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.M) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithModifiers(
                            Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC);
            gson = gsonBuilder.create();
        } else {
            gson = new Gson();
        }
    }

    private GsonHolder() {
        throw new UnsupportedOperationException();
    }

    public static Gson getGson() {
        return gson;
    }
}

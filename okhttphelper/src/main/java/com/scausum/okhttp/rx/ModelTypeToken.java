package com.scausum.okhttp.rx;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by xin on 4/26/17.
 */

public abstract class ModelTypeToken<T> {

    private Type type;

    public ModelTypeToken() {
        type = getSuperclassTypeParameter(getClass());
    }

    /**
     * Returns the type from super class's type parameter in canonical form.
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}

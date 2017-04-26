package com.scausum.okhttp.rx;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.scausum.okhttp.ModelParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xin on 4/25/17.
 */

public class ModelObservableOnSubscribe<T> implements ObservableOnSubscribe<T> {

    private Type modelType;
    private Call call;

    public static <T> ModelObservableOnSubscribe<T> create() {
        return new ModelObservableOnSubscribe<T>(){};
    }

    public ModelObservableOnSubscribe() {
        modelType = genericModelType();
    }

    protected Type genericModelType() {
        return getGenericTypeParameter(getClass());
    }

    protected static Type getGenericTypeParameter(Class<?> thisClass) {
        Type genericSuperclass = thisClass.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing modelType parameter.");
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type type = parameterizedType.getActualTypeArguments()[0];
        Log.e("Model", type.toString());
        return type;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {
        if (call == null) {
            emitter.onError(new NullPointerException("The okhttp call is null;"));
            return;
        }
        Response response = call.execute();
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            T model = null;
            try {
                if (modelType == String.class) {
                    //noinspection unchecked
                    model = (T) responseBody.string();
                } else {
                    model = ModelParser.parseResponseToModel(responseBody, modelType);
                }
            } catch (Exception e) {
                emitter.onError(e);
                return;
            } finally {
                responseBody.close();
            }
            if (model == null) {
                emitter.onError(new NetworkErrorException("The model parse is null."));
                return;
            } else {
                emitter.onNext(model);
            }
        } else {
            emitter.onError(new NetworkErrorException(response.toString()));
            return;
        }

        emitter.onComplete();
    }
}

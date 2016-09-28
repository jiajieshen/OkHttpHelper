package com.fubaisum.okhttpsample;

/**
 * Created by sum on 5/17/16.
 */
public class Api<T> {
    boolean status;
    String msg;
    T data;

    @Override
    public String toString() {
        return "Api{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

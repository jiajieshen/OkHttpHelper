package com.fubaisum.okhttpsample;

import java.util.List;

/**
 * Created by sum on 5/23/16.
 */
public class TagWrapper<T> {
    public int type;
    public List<T> values;

    @Override
    public String toString() {
        return "TagWrapper{" +
                "type=" + type +
                ", values=" + values +
                '}';
    }
}

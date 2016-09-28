package com.fubaisum.okhttpsample;

/**
 * Created by sum on 5/10/16.
 */
public class User {
    public String name;
    public String avatar;
    public String address;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

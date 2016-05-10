package com.fubaisum.okhttpsample;

/**
 * Created by sum on 5/10/16.
 */
public class User {
    public String name;
    public String birthday;
    public String region;
    public String address;
    public String avatar;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", region='" + region + '\'' +
                ", address='" + address + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}

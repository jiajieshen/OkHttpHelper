package com.fubaisum.okhttpsample;

/**
 * Created by sum on 5/10/16.
 */
public class User {
    public String nickname;
    public String headimgurl;
    public String phone;

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

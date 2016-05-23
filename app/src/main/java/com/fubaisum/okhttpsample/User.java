package com.fubaisum.okhttpsample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sum on 5/10/16.
 */
public class User {
    public String nickname;
    public String avatar;
    @SerializedName("referral_code")
    public String activationCode;

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", activationCode='" + activationCode + '\'' +
                '}';
    }
}

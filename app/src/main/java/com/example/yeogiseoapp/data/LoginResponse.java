package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("username")
    private String username;

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }
}

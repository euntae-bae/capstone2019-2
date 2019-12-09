package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class FindUserResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("username")
    private String username;
    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public String getUsername() {
        return username;
    }
    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}

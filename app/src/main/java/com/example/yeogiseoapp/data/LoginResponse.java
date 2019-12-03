package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("username")
    private String username;
    @SerializedName("id")
    private int id;
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
    public int getId() {
        return code;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}

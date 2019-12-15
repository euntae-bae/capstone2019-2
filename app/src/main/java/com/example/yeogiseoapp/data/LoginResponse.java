package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 로그인 과정 이후 서버로부터 반환받은 데이터
public class LoginResponse {
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

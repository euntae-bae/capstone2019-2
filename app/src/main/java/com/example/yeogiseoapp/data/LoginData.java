package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 로그인 과정에서 서버에 보내는 데이터
public class LoginData {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

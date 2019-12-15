package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 회원가입 과정을 위해 서버로 보내는 데이터
public class RegisterData {
    @SerializedName("email")
    private String email;
    @SerializedName("userName")
    private String userName;
    @SerializedName("password")
    private String password;

    public RegisterData(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}

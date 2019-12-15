package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// email로 user의 정보를 찾기위해 서버측에 보내는 데이터
public class FindUserData {
    @SerializedName("email")
    private String email;

    public FindUserData(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}

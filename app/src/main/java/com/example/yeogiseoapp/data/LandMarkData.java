package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 이미지 대상 정보 검색을 하기 위해 서버측에 보내는 데이터
public class LandMarkData {
    @SerializedName("pathname")
    private String pathname;

    public LandMarkData(String data) {
        this.pathname = data;
    }

    public String getPathname() {
        return pathname;
    }
}
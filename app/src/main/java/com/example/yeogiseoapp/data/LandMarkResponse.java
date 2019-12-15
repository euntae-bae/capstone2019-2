package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 이미지 정보 검색 이후 서버로부터 반환받은 데이터
public class LandMarkResponse {

    @SerializedName("name")
    private String landmarkName;

    @SerializedName("value")
    private  float score;


    public float getScore() {
        return score;
    }

    public String getLandmarkName() {
        return landmarkName;
    }
}

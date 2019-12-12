package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

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

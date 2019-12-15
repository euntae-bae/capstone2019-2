package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

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
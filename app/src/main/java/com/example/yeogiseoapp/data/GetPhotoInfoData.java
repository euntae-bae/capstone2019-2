package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetPhotoInfoData {

    @SerializedName("gid")
    private int gid;

    public GetPhotoInfoData(int i) {
        gid = i;
    }

    public int getId() {
        return gid;
    }

}

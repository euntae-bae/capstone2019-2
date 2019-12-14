package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class RemoveImageData {
    @SerializedName("imageID")
    private int imgaeID;

    public RemoveImageData(int iid) {
        imgaeID = iid;
    }

    public int getImgaeID() {
        return imgaeID;
    }
}

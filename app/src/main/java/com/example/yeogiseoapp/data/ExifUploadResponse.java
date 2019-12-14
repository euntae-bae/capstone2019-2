package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExifUploadResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("imdID")
    private ArrayList<Object> imgIDList;

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public ArrayList<Object> getImgIDList() { return imgIDList; }
    public String getimgID(int i) {
        return imgIDList.get(i).toString();
    }
}

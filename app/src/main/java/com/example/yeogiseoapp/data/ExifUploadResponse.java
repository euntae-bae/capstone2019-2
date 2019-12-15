package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

// EXIF 업로드 이후 서버로부터 반환받은 데이터
public class ExifUploadResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}

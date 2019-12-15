package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 이미지를 삭제한 이후 서버로부터 반환되는 데이터
public class RemoveImageResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
}

package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 코멘트 설정 이후 서버에서 반환된 데이터
public class CommentResponse {
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

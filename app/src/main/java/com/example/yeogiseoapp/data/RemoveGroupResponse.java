package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 그룹 삭제 이후 서버로부터 반환받은 데이터
public class RemoveGroupResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
}

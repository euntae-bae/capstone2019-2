package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 그룹에서 나온 뒤 서버로부터 반환받은 데이터
public class ExitGroupResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
}

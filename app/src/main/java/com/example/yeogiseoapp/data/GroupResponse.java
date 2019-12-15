package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 그룹을 생성한 뒤 서버로부터 반환받은 데이터
public class GroupResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("groupID")
    private int groupID;

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
    public int getGroupID() {
        return groupID;
    }
}

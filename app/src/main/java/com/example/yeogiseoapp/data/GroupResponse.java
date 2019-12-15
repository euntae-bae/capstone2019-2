package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

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

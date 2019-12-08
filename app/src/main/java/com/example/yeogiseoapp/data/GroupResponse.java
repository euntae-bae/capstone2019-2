package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class GroupResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("groupID")
    private String groupID;

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
    public String getGroupID() {
        return groupID;
    }
}

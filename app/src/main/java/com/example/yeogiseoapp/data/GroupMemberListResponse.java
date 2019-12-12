package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

public class GroupMemberListResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("list")
    private ArrayList<Object> list = new ArrayList<>();

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
    public ArrayList<Object> getList() { return list; }

    public String getListIndexUserName(int i){
        return list.get(i).toString();
    }

}

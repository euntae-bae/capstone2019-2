package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

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

package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class GroupInquiryData {
    @SerializedName("id")
    private String id;

    public GroupInquiryData(String uid) {
        id = uid;
    }

    public String getId() {
        return id;
    }
}

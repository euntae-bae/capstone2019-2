package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class GroupInquiryData {
    @SerializedName("id")
    private int id;

    public GroupInquiryData(int uid) {
        id = uid;
    }

    public int getId() {
        return id;
    }
}

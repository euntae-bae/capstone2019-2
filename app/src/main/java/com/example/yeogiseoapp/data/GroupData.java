package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class GroupData {
    @SerializedName("userID")
    private String userID;
    @SerializedName("groupName")
    private String groupName;

    public GroupData(String uid, String gname) {
        userID = uid;
        groupName = gname;
    }

    public String getUserID() {
        return userID;
    }

    public String getGroupName() {
        return groupName;
    }
}

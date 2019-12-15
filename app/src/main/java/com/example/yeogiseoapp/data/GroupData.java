package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class GroupData {
    @SerializedName("userID")
    private int userID;
    @SerializedName("groupName")
    private String groupName;

    public GroupData(int uid, String gname) {
        userID = uid;
        groupName = gname;
    }

    public int getUserID() {
        return userID;
    }

    public String getGroupName() {
        return groupName;
    }
}

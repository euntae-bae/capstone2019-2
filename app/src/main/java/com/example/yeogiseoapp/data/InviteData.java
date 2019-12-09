package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class InviteData {
    @SerializedName("groupID")
    private String groupID;
    @SerializedName("userID")
    private String userID;

    public InviteData(String gid, String uid) {
        groupID = gid;
        userID = uid;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getUserID() {
        return userID;
    }
}

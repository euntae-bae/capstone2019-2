package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class InviteData {
    @SerializedName("groupID")
    private int groupID;
    @SerializedName("userID")
    private int userID;

    public InviteData(int gid, int uid) {
        groupID = gid;
        userID = uid;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getUserID() {
        return userID;
    }
}

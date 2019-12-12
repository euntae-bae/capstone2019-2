package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class ExitGroupData {
    @SerializedName("groupID")
    private String groupID;
    @SerializedName("userID")
    private String userID;

    public ExitGroupData(String gid, String uid) {
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

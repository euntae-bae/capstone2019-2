package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class RemoveGroupData {
    @SerializedName("groupID")
    private String groupID;

    public RemoveGroupData(String gid) {
        groupID = gid;
    }

    public String getGroupID() {
        return groupID;
    }
}

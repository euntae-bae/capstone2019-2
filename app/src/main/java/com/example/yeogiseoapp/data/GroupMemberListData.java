package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class GroupMemberListData {
    @SerializedName("groupID")
    private String groupID;

    public GroupMemberListData(String gid) {
        groupID = gid;
    }

    public String getGroupID() {
        return groupID;
    }
}

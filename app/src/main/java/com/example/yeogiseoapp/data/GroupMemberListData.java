package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class GroupMemberListData {
    @SerializedName("groupID")
    private int groupID;

    public GroupMemberListData(int gid) {
        groupID = gid;
    }

    public int getGroupID() {
        return groupID;
    }
}

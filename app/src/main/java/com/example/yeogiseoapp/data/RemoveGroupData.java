package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class RemoveGroupData {
    @SerializedName("groupID")
    private int groupID;

    public RemoveGroupData(int gid) {
        groupID = gid;
    }

    public int getGroupID() {
        return groupID;
    }
}

package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class ScheduleData {
    @SerializedName("groupID")
    private int groupID;

    public ScheduleData(int gid) {
        groupID = gid;
    }

    public int getGroupID() {
        return groupID;
    }
}

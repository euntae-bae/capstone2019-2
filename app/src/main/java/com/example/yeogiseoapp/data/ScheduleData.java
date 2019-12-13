package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

public class ScheduleData {
    @SerializedName("groupID")
    private String groupID;

    public ScheduleData(String gid) {
        groupID = gid;
    }

    public String getGroupID() {
        return groupID;
    }
}

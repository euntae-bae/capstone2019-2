package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 일정 정보를 받기 위해 서버로 보내는 데이터
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

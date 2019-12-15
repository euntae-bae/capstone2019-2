package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 그룹을 삭제하기 위해 서버로 보내는 데이터
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

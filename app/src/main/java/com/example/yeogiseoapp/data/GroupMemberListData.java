package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 해당 그룹에 속한 멤버들을 알기 위해 서버측에 보내는 데이터
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

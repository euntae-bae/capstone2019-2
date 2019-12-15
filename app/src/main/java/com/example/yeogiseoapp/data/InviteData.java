package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 그룹에 유저를 초대하기 위해 서버로 보내는 데이터
public class InviteData {
    @SerializedName("groupID")
    private int groupID;
    @SerializedName("userID")
    private int userID;

    public InviteData(int gid, int uid) {
        groupID = gid;
        userID = uid;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getUserID() {
        return userID;
    }
}

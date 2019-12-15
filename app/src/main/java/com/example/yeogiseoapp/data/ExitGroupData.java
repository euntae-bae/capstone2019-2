package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 그룹 나가기에 쓰이는 서버측에 보낼 데이터
public class ExitGroupData {
    @SerializedName("groupID")
    private int groupID;
    @SerializedName("userID")
    private int userID;

    public ExitGroupData(int gid, int uid) {
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

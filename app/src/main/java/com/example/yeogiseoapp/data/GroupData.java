package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 그룹을 생성하기 위해 서버측에 보내는 데이터
public class GroupData {
    @SerializedName("userID")
    private int userID;
    @SerializedName("groupName")
    private String groupName;

    public GroupData(int uid, String gname) {
        userID = uid;
        groupName = gname;
    }

    public int getUserID() {
        return userID;
    }

    public String getGroupName() {
        return groupName;
    }
}

package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 해당 유저가 속한 그룹을 알기 위해 서버측에 보내는 데이터
public class GroupInquiryData {
    @SerializedName("id")
    private int id;

    public GroupInquiryData(int uid) {
        id = uid;
    }

    public int getId() {
        return id;
    }
}

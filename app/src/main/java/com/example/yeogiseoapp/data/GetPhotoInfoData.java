package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

// 그룹에 속한 이미지 정보를 불러오기 위해 서버측에 보내는 데이터
public class GetPhotoInfoData {

    @SerializedName("gid")
    private int gid;

    public GetPhotoInfoData(int i) {
        gid = i;
    }

    public int getId() {
        return gid;
    }

}

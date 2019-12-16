package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 이미지를 삭제하기 위해 서버로 보내는 데이터
public class RemoveImageData {
    @SerializedName("imageID")
    private int imgaeID;
    @SerializedName("groupID")
    private int gid;

    public RemoveImageData(int iid, int gd) {
        imgaeID = iid;
        gid = gd;
    }

    public int getImgaeID() {
        return imgaeID;
    }
    public int getGid() {
        return gid;
    }
}

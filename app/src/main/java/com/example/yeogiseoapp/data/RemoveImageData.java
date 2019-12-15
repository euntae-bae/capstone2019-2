package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 이미지를 삭제하기 위해 서버로 보내는 데이터
public class RemoveImageData {
    @SerializedName("imageID")
    private int imgaeID;

    public RemoveImageData(int iid) {
        imgaeID = iid;
    }

    public int getImgaeID() {
        return imgaeID;
    }
}

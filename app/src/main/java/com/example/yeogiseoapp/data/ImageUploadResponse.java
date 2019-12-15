package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// 이미지를 업로드한 뒤 서버로부터 반환받은 데이터
public class ImageUploadResponse {


    @SerializedName("name")
    private String message;

    public String getMessage() {
        return message;
    }


}

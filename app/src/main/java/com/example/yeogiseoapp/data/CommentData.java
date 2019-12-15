package com.example.yeogiseoapp.data;

import com.example.yeogiseoapp.PhotoInfo;
import com.google.gson.annotations.SerializedName;

// 코멘트 설정을 위해 서버측에 보내는 데이터
public class CommentData {
    @SerializedName("imageID")
    private int imageID;
    @SerializedName("imagePath")
    private String imagePath;
    @SerializedName("userID")
    private int userID;
    @SerializedName("username")
    private String username;
    @SerializedName("comment")
    private String comment;

    public CommentData(int iid, String ipath, int uid, String uname, String com) {
        imageID = iid;
        imagePath = ipath;
        userID = uid;
        username = uname;
        comment = com;
    }

    public int getImageID() { return imageID; }
    public String getImagePath() {
        return imagePath;
    }
    public int getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getComment() {
        return comment;
    }
}

package com.example.yeogiseoapp;

import android.graphics.drawable.Drawable;

// 그룹이 가지는 데이터들을 정의해놓은 클래스이다.
public class GroupInfoItem {
    private Drawable iconDrawble;
    private String roomStr ;
    private String infoStr ;
    private int groupID;

    public void setIcon(Drawable icon) {
        iconDrawble = icon;
    }

    public void setRoom(String title) {
        roomStr = title ;
    }
    public void setInfo(String desc) {
        infoStr = desc ;
    }
    public void setGroupID(int gid) {
        groupID = gid ;
    }

    public Drawable getIcon() {
        return iconDrawble;
    }

    public String getRoom() {
        return this.roomStr;
    }
    public String getInfo() {
        return this.infoStr;
    }
    public int getGroupID() {
        return this.groupID;
    }
}
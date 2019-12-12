package com.example.yeogiseoapp;

import android.graphics.drawable.Drawable;

public class listViewItem {
    private Drawable iconDrawble;
    private String roomStr ;
    private String infoStr ;
    private String groupID;

    public void setIcon(Drawable icon) {
        iconDrawble = icon;
    }

    public void setRoom(String title) {
        roomStr = title ;
    }
    public void setInfo(String desc) {
        infoStr = desc ;
    }
    public void setGroupID(String gid) {
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
    public String getGroupID() {
        return this.groupID;
    }
}
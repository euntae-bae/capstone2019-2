package com.example.yeogiseoapp;

import android.graphics.drawable.Drawable;

public class GroupMemberItem {
    private String name ;
    private String email;

    public void setName(String n) {
        name = n;
    }

    public void setEmail(String e) {
        email = e ;
    }

    public String getName() {
        return this.name;
    }
    public String getEmail() {
        return this.email;
    }
}
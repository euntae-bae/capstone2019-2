package com.example.yeogiseoapp;

import android.graphics.drawable.Drawable;

//각 그룹 멤버들이 가지는 데이터를 정의해놓은 클래스이다.
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
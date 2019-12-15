package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

// 해당 그룹에 속한 멤버들의 정보를 서버로부터 반환받은 데이터
public class GroupMemberListResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("count")
    private int count;
    @SerializedName("list")
    private ArrayList<Object> list = new ArrayList<>();

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
    public int getCount() {
        return count;
    }
    public ArrayList<Object> getList() { return list; }

    public String getListIndexEmail(int i){
        String temp =  list.get(i).toString();
        int eStart = temp.indexOf("email=")+6;
        int eEnd = temp.indexOf(", username=");
        return temp.substring(eStart, eEnd);
    }

    public String getListIndexUserName(int i){
        String temp =  list.get(i).toString();
        int nStart = temp.indexOf("username=") + 9;
        int nEnd = temp.indexOf(", password=");
        return temp.substring(nStart, nEnd);
    }

    public ArrayList<String> getNameList(){
        ArrayList<String> res = new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            res.add(getListIndexUserName(i));

        return res;
    }

    public ArrayList<String> getEmailList(){
        ArrayList<String> res = new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            res.add(getListIndexEmail(i));

        return res;
    }

}

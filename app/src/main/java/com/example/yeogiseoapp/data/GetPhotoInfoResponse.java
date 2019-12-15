package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

// 그룹에 속한 사진들의 정보를 서버로부터 반환받은 데이터
public class GetPhotoInfoResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("size")
    private int size;
    @SerializedName("list")
    private ArrayList<Object> list;

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
    public int getSize() { return size; }
    public ArrayList<Object> getList() { return list; }

    public int getImgId(int i){
        String temp =  list.get(i).toString();
        String start = "id=";
        String end = ", pathname=";
        int oStart = temp.indexOf(start)+start.length();
        int oEnd = temp.indexOf(end);
        return Math.round(Float.parseFloat(temp.substring(oStart, oEnd)));
    }

    public String getImgPathname(int i){
        String temp =  list.get(i).toString();
        String start = "pathname=";
        String end = ", longitude=";
        int oStart = temp.indexOf(start)+start.length();
        int oEnd = temp.indexOf(end);
        return temp.substring(oStart, oEnd);
    }

    public float getImgLongitude(int i){
        String temp =  list.get(i).toString();
        String start = "longitude=";
        String end = ", latitude=";
        int oStart = temp.indexOf(start)+start.length();
        int oEnd = temp.indexOf(end);
        return Float.parseFloat(temp.substring(oStart, oEnd));
    }

    public float getImgLatitude(int i){
        String temp =  list.get(i).toString();
        String start = "latitude=";
        String end = ", dateAndTime=";
        int oStart = temp.indexOf(start)+start.length();
        int oEnd = temp.indexOf(end);
        return Float.parseFloat(temp.substring(oStart, oEnd));
    }

    public String getImgdateAndTime(int i){
        String temp =  list.get(i).toString();
        String start = "dateAndTime=";
        String end = ", groupID=";
        int oStart = temp.indexOf(start)+start.length();
        int oEnd = temp.indexOf(end);
        return temp.substring(oStart, oEnd);
    }

    public int getImgGroupID(int i){
        String temp =  list.get(i).toString();
        String start = "groupID=";
        String end = ", userID=";
        int oStart = temp.indexOf(start)+start.length();
        int oEnd = temp.indexOf(end);
        return Math.round(Float.parseFloat(temp.substring(oStart, oEnd)));
    }

    public int getImgUserID(int i){
        String temp =  list.get(i).toString();
        String start = "userID=";
        String end = "}";
        int oStart = temp.indexOf(start)+start.length();
        int oEnd = temp.lastIndexOf(end);
        return Math.round(Float.parseFloat(temp.substring(oStart, oEnd)));
    }
}

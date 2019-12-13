package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ScheduleResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("count")
    private int count;
    @SerializedName("list")
    private ArrayList<Object> list = new ArrayList<>();
    private ArrayList<String> imgList = new ArrayList<>();

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
    public int getCount() {
        return count;
    }
    public ArrayList<Object> getList() { return list; }

    public Object getObj(int i){ return list.get(i); }

    public float getListIndexLongitude(int i){
        String temp =  list.get(i).toString();
        int oStart = temp.indexOf("longitude=")+10;
        int oEnd = temp.indexOf(", dateAndTime=");
        return Float.parseFloat(temp.substring(oStart, oEnd));
    }

    public float getListIndexLatitude(int i){
        String temp =  list.get(i).toString();
        int aStart = temp.indexOf("latitude=") + 9;
        int aEnd = temp.indexOf(", longitude=");
        return Float.parseFloat(temp.substring(aStart, aEnd));
    }

    public long getListIndexTime(int i){
        String temp =  list.get(i).toString();
        int tStart = temp.indexOf("dateAndTime=") + 12;
        int tEnd = temp.indexOf(", imageList=");
        temp = temp.substring(tStart, tEnd).replaceAll("[- :TZ.]", "");
        return Long.parseLong(temp);
    }

    public long getListIndexImgList(int i){
        String temp =  list.get(i).toString();
        int tStart = temp.indexOf("imageList=") + 10;
        int tEnd = temp.indexOf("}");
        temp = temp.substring(tStart, tEnd).replaceAll("[- :TZ.]", "");
        return Long.parseLong(temp);
    }


    /*

    public ArrayList<String> getLongitudeList(){
        ArrayList<String> res = new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            res.add(getListIndexLongitude(i));

        return res;
    }

    public ArrayList<String> getLatitudeList(){
        ArrayList<String> res = new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            res.add(getListIndexLatitude(i));

        return res;
    }

    public ArrayList<String> getTimeList(){
        ArrayList<String> res = new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            res.add(getListIndexLatitude(i));

        return res;
    }
    */
}

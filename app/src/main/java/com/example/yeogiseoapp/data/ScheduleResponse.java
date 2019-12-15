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

    public int getCode() {
        return code;
    }
    public String getMessage() { return message; }
    public int getCount() {
        return count;
    }

    public int getListIndexScheduleId(int i){
        String temp =  list.get(i).toString();
        int oStart = temp.indexOf("scheduleID=")+11;
        int oEnd = temp.indexOf(", latitude=");
        return Math.round(Float.parseFloat(temp.substring(oStart, oEnd)));
    }

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

    public long getListIndexTimeLong(int i){
        String temp =  list.get(i).toString();
        int tStart = temp.indexOf("dateAndTime=") + 12;
        int tEnd = temp.indexOf(", imageList=");
        temp = temp.substring(tStart, tEnd).replaceAll("[- :TZ.]", "");
        return Long.parseLong(temp);
    }

    public String getListIndexTimeString(int i){
        String temp =  list.get(i).toString();
        int tStart = temp.indexOf("dateAndTime=") + 12;
        int tEnd = temp.indexOf(", imageList=");
        temp = temp.substring(tStart, tEnd);
        return temp;
    }

    public ArrayList<Integer> getImgList(int i){
        String temp = list.get(i).toString();
        String[] h;
        ArrayList<Integer> imgList = new ArrayList<>();
        int tStart = temp.indexOf("imageList=,") + 11;
        int tEnd = temp.indexOf("}");
        temp = temp.substring(tStart, tEnd);
        h = temp.split(",");
        for(int j=0; j<h.length; j++) {
            imgList.add(Integer.parseInt(h[j]));
        }
        return imgList;
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

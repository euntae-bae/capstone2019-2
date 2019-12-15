package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

// EXIF 설정을 위해 서버측에 보내는 데이터
public class ExifData {

    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private Float latitude;
    @SerializedName("longitude")
    private Float longitude;
    @SerializedName("time")
    private long time;
    @SerializedName("id")
    private int id;
    @SerializedName("gid")
    private int gid;

    public ExifData(){}

    public ExifData(String name, Float longitude, Float latitude, long time, int id, int gid){
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.id = id;
        this.gid = gid;
    }




    public void setName(String name) {
        this.name = name;
    }
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }
    public Float getLatitude() {
        return latitude;
    }
    public Float getLongitude() {
        return longitude;
    }
    public long getTime() {
        return time;
    }

    public int getGid() {
        return gid;
    }

    public int getId() {
        return id;
    }
}

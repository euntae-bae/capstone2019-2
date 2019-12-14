package com.example.yeogiseoapp.data;

import com.google.gson.annotations.SerializedName;

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
    private String id;
    @SerializedName("gid")
    private String gid;

    public ExifData(){}

    public ExifData(String name, Float longitude, Float latitude, long time, String id, String gid){
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

    public String getGid() {
        return gid;
    }

    public String getId() {
        return id;
    }
}

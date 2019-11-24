package com.example.yeogiseoapp;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class PhotoInfo {
    Uri uri;
    long time;
    float latitude;
    float longitude;

    PhotoInfo(){
        uri = null;
        time = -1;
        latitude = -1;
        longitude = -1;
    }

    PhotoInfo(Uri u){
        uri = u;
        time = -1;
        latitude = -1;
        longitude = -1;
    }
    PhotoInfo(Uri u, long t, float la, float lo){
        uri = u;
        time = t;
        latitude = la;
        longitude = lo;
    }

    PhotoInfo(Uri u, String t, String la, String lo){
        uri = u;
        time = convertToTime(t);
        latitude = convertToDegree(la);
        longitude = convertToDegree(lo);
    }
    public void setInfo(Uri u, long t, float la, float lo){
        uri = u;
        time = t;
        latitude = la;
        longitude = lo;
    }

    public void setInfo(Uri u, String t, String la, String lo){
        uri = u;
        time = convertToTime(t);
        latitude = convertToDegree(la);
        longitude = convertToDegree(lo);
    }

    public void setUri(Uri u){
        uri = u;
    }

    public void setTime(long t){
        time = t;
    }
    public void setTime(String t){
        time = convertToTime(t);
    }

    public void setLatitude(float l){
        latitude = l;
    }
    public void setLatitude(String l){
        latitude = convertToDegree(l);
    }

    public void setLongitude(float l){
        longitude = l;
    }
    public void setLongitude(String l){
        longitude = convertToDegree(l);
    }

    private float convertToDegree(String stringDMS){
        float result;
        if(stringDMS == null) {
            result = -1;
            return result;
        }
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        float D0 = Float.parseFloat(stringD[0]);
        float D1 = Float.parseFloat(stringD[1]);
        float FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        float M0 = Float.parseFloat(stringM[0]);
        float M1 = Float.parseFloat(stringM[1]);
        float FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        float S0 = Float.parseFloat(stringS[0]);
        float S1 = Float.parseFloat(stringS[1]);
        float FloatS = S0/S1;

        result = FloatD + (FloatM/60) + (FloatS/3600);

        return result;
    };

    private long convertToTime(String stringDMS){
        long result = 0;
        String strRes = "";
        for(int i=0; i<stringDMS.length(); i++){
            if(stringDMS.charAt(i) != ':' && stringDMS.charAt(i) != ' ')
                strRes += stringDMS.charAt(i);
        }
        result = Long.parseLong(strRes);

        return result;
    };
}

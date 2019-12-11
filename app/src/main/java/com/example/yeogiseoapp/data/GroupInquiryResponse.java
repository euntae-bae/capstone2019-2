package com.example.yeogiseoapp.data;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupInquiryResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("size")
    private int size;
    @SerializedName("arr")
    private ArrayList arr;

    private ArrayList<GroupListData> groupArr = new ArrayList<>();

    private class GroupListData{
        private String groupName = null;
        private String creator = null;

        GroupListData(String n, String c){
            groupName = n;
            creator = c;
        }

        public void setData(String n, String c){
            groupName = n;
            creator = c;
        }

        public String getGroupName() { return groupName; }
        public String getCreator() { return creator; }
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public int getSize() {
        return size;
    }
    public void setGroupArr() {
        String strArr = String.valueOf(arr);
        for(int i=0; i<size; i++) {
            int nStart = strArr.indexOf("groupName=") + 10;
            int nEnd = strArr.indexOf(", username=");
            int cStart = strArr.indexOf("username=") + 9;
            int cEnd = strArr.indexOf("}");
            int delPt = (i != size-1) ? cEnd + 3 : cEnd;
            String n = strArr.substring(nStart, nEnd);
            String c = strArr.substring(cStart, cEnd);
            groupArr.add(new GroupListData(n, c));
            strArr = strArr.substring(delPt);
        }
    };

    public String getIndexGroupName(int i){ return groupArr.get(i).getGroupName(); }
    public String getIndexCreator(int i){ return groupArr.get(i).getCreator(); }
}

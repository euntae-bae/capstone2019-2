package com.example.yeogiseoapp;

import java.util.ArrayList;

// 일정 정보가 담기기 위해 정의된 클래스이다.
public class ScheduleInfo {
    int scheduleID;
    float latitude;
    float longitude;
    String dateAndTime = null;
    ArrayList<Integer> imageList = new ArrayList<>();
}

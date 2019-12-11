package com.example.yeogiseoapp;

import com.example.yeogiseoapp.data.FindUserData;
import com.example.yeogiseoapp.data.FindUserResponse;
import com.example.yeogiseoapp.data.GroupData;
import com.example.yeogiseoapp.data.GroupResponse;
import com.example.yeogiseoapp.data.GroupInquiryData;
import com.example.yeogiseoapp.data.GroupInquiryResponse;
import com.example.yeogiseoapp.data.InviteData;
import com.example.yeogiseoapp.data.InviteResponse;
import com.example.yeogiseoapp.data.LoginData;
import com.example.yeogiseoapp.data.LoginResponse;
import com.example.yeogiseoapp.data.RegisterData;
import com.example.yeogiseoapp.data.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/login")
    Call<LoginResponse> userLogin(@Body LoginData data);
    @POST("/register")
    Call<RegisterResponse> userRegister(@Body RegisterData data);
    @POST("/make-group")
    Call<GroupResponse> groupMake(@Body GroupData data);
    @POST("/invite")
    Call<InviteResponse> invite(@Body InviteData data);
    @POST("/find-user")
    Call<FindUserResponse> findUser(@Body FindUserData data);
    @POST("/group-inquiry")
    Call<GroupInquiryResponse> groupInquiry(@Body GroupInquiryData data);
}

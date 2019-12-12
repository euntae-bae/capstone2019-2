package com.example.yeogiseoapp;

import com.example.yeogiseoapp.data.FindUserData;
import com.example.yeogiseoapp.data.FindUserResponse;
import com.example.yeogiseoapp.data.GroupData;
import com.example.yeogiseoapp.data.GroupMemberListData;
import com.example.yeogiseoapp.data.GroupMemberListResponse;
import com.example.yeogiseoapp.data.GroupResponse;
import com.example.yeogiseoapp.data.GroupInquiryData;
import com.example.yeogiseoapp.data.GroupInquiryResponse;
import com.example.yeogiseoapp.data.InviteData;
import com.example.yeogiseoapp.data.InviteResponse;
import com.example.yeogiseoapp.data.LoginData;
import com.example.yeogiseoapp.data.LoginResponse;
import com.example.yeogiseoapp.data.RegisterData;
import com.example.yeogiseoapp.data.RegisterResponse;
import com.example.yeogiseoapp.data.ExitGroupData;
import com.example.yeogiseoapp.data.ExitGroupResponse;

import java.util.List;

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
    @POST("/group-exit")
    Call<ExitGroupResponse> exitGroup(@Body ExitGroupData data);
    @POST("/group-member-list")
    Call<GroupMemberListResponse> groupMemberList(@Body GroupMemberListData data);

}

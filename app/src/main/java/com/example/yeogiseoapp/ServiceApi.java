package com.example.yeogiseoapp;

import com.example.yeogiseoapp.data.CommentData;
import com.example.yeogiseoapp.data.CommentResponse;
import com.example.yeogiseoapp.data.FindUserData;
import com.example.yeogiseoapp.data.FindUserResponse;
import com.example.yeogiseoapp.data.GetPhotoInfoData;
import com.example.yeogiseoapp.data.GetPhotoInfoResponse;
import com.example.yeogiseoapp.data.GroupData;
import com.example.yeogiseoapp.data.GroupMemberListData;
import com.example.yeogiseoapp.data.GroupMemberListResponse;
import com.example.yeogiseoapp.data.GroupResponse;
import com.example.yeogiseoapp.data.GroupInquiryData;
import com.example.yeogiseoapp.data.GroupInquiryResponse;
import com.example.yeogiseoapp.data.InviteData;
import com.example.yeogiseoapp.data.InviteResponse;
import com.example.yeogiseoapp.data.ExifData;
import com.example.yeogiseoapp.data.ExifUploadResponse;
import com.example.yeogiseoapp.data.ImageUploadResponse;
import com.example.yeogiseoapp.data.LandMarkData;
import com.example.yeogiseoapp.data.LandMarkResponse;
import com.example.yeogiseoapp.data.LoginData;
import com.example.yeogiseoapp.data.LoginResponse;
import com.example.yeogiseoapp.data.RegisterData;
import com.example.yeogiseoapp.data.RegisterResponse;
import com.example.yeogiseoapp.data.ExitGroupData;
import com.example.yeogiseoapp.data.ExitGroupResponse;
import com.example.yeogiseoapp.data.RemoveGroupData;
import com.example.yeogiseoapp.data.RemoveGroupResponse;
import com.example.yeogiseoapp.data.RemoveImageData;
import com.example.yeogiseoapp.data.RemoveImageResponse;
import com.example.yeogiseoapp.data.ScheduleData;
import com.example.yeogiseoapp.data.ScheduleResponse;

import java.util.List;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

// aws 서버와 통신하기 위한 인터페이스를 정의해놓은 파일이다.
public interface ServiceApi {

    String URL = "ec2-54-180-107-241.ap-northeast-2.compute.amazonaws.com";
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
    @POST("/remove-group")
    Call<RemoveGroupResponse> removeGroup(@Body RemoveGroupData data);
    @POST("/exif")
    Call<List<ExifUploadResponse>> exifUpload(@Body ArrayList<ExifData> data);
    @Multipart
    @POST("/upload")
    Call<ImageUploadResponse> imageUpload(@Part MultipartBody.Part file, @Part("name") RequestBody body);
    @Multipart
    @POST("/upload")
    Call<ImageUploadResponse> imageUploadDynamic(
            @Part("description") RequestBody desc,
            @Part List<MultipartBody.Part> files
            );
    @POST("/api")
    Call<LandMarkResponse> getLandMark(@Body LandMarkData path);

    @POST("/schedule")
    Call<ScheduleResponse> getSchedule(@Body ScheduleData data);

    @POST("/get-image-info")
    Call<GetPhotoInfoResponse> getPhotoInfo(@Body GetPhotoInfoData data);

    @POST("/remove-image")
    Call<RemoveImageResponse> removeImage(@Body RemoveImageData data);

    @POST("/comment")
    Call<CommentResponse> comment(@Body CommentData data);
}

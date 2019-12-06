package com.example.yeogiseoapp;

import com.example.yeogiseoapp.data.ExifData;
import com.example.yeogiseoapp.data.ExifUploadResponse;
import com.example.yeogiseoapp.data.ImageUploadResponse;
import com.example.yeogiseoapp.data.LoginData;
import com.example.yeogiseoapp.data.LoginResponse;
import com.example.yeogiseoapp.data.RegisterData;
import com.example.yeogiseoapp.data.RegisterResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceApi {
    @POST("/login")
    Call<LoginResponse> userLogin(@Body LoginData data);
    @POST("/register")
    Call<RegisterResponse> userRegister(@Body RegisterData data);
    @POST("/getdata")
    Call<ExifUploadResponse> exifUpload(@Body ArrayList<ExifData> data);
    @POST("/upload")
    Call<ImageUploadResponse> imageUpload(@Part MultipartBody.Part file, @Part("name") RequestBody body);
    @POST("/upload")
    Call<ImageUploadResponse> imageUploadDynamic(
            @Part List<MultipartBody.Part> files,
            @Part("description") RequestBody desc
            );
}

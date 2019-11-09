package com.example.yeogiseoapp;

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
}

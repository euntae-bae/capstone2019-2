package com.example.yeogiseoapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static int PORT = 3000;
    private final static String BASE_URL = "http://ec2-54-180-107-241.ap-northeast-2.compute.amazonaws.com:" + PORT;
    private static Retrofit retrofit = null;

    private RetrofitClient() { }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
package com.example.yeogiseoapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.yeogiseoapp.ui.login.LoginActivity;


// 로그인 정보가 있다면 자동 로그인을 하고 없다면 로그인 화면으로 넘어가는 자동 로그인 액티비티이다.
public class StartupActivity extends Activity {
    private SharedPreferences sp;
    //private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("loggedinData", MODE_PRIVATE);
        String email = sp.getString("loggedinEmail", null);
        String password = sp.getString("loggedinPassword", null);
        String nickname = sp.getString("loggedinUsername", null);
        String id = sp.getString("loggedinId", null);
        Log.i("autologin", "email: " + email + "/password: " + password);

        Intent intent = null;
        if (email == null || password == null || nickname == null || id == null) {
            Log.i("autologin", "failed");
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        else {
            Log.i("autologin", "succeed");
            intent = new Intent(getApplicationContext(), HallActivity.class);
        }


        startActivity(intent);
    }
}

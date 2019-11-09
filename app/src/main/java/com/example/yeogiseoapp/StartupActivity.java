package com.example.yeogiseoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.yeogiseoapp.ui.login.LoginActivity;

public class StartupActivity extends Activity {
    private SharedPreferences sp;
    //private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("loggedinData", MODE_PRIVATE);
        String email = sp.getString("loggedinEmail", null);
        String password = sp.getString("loggedinPassword", null);
        Log.i("autologin", "email: " + email + "/password: " + password);

        Intent intent = null;
        if (email == null || password == null) {
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

package com.example.yeogiseoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HallActivity extends AppCompatActivity {
    TextView welcome;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);

        welcome = (TextView)findViewById(R.id.welcomeText);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        welcome.setText(id + "님의 방문을 환영합니다");
    }
}
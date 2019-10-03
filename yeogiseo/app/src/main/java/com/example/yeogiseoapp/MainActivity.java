package com.example.yeogiseoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText eid;
    EditText epw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button)findViewById(R.id.loginBtn);
        eid = (EditText)findViewById(R.id.editID);
        epw = (EditText)findViewById(R.id.editPassword);
    }

    public void login_func(View v)
    {
        String id = eid.getText().toString();
        String pw = epw.getText().toString();

        Intent intent = new Intent(MainActivity.this, HallActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
}

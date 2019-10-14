package com.example.yeogiseoapp.ui.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yeogiseoapp.R;

public class RegisterActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText edtTextEmail = (EditText) findViewById(R.id.edtTextEmail);
        final EditText edtTextUsername = (EditText) findViewById(R.id.edtTextUsername);
        final EditText edtTextPass = (EditText) findViewById(R.id.edtTextPass);
        final Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}

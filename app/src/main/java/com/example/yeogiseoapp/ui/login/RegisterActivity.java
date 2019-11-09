package com.example.yeogiseoapp.ui.login;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yeogiseoapp.R;
import com.example.yeogiseoapp.RetrofitClient;
import com.example.yeogiseoapp.ServiceApi;
import com.example.yeogiseoapp.data.RegisterData;
import com.example.yeogiseoapp.data.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends Activity {
    private EditText edtTextEmail = null;
    private EditText edtTextUsername = null;
    private EditText edtTextPass = null;
    private ServiceApi service = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtTextEmail = (EditText)findViewById(R.id.edtTextEmail);
        edtTextUsername = (EditText)findViewById(R.id.edtTextUsername);
        edtTextPass = (EditText)findViewById(R.id.edtTextPass);

        final Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        final Button btnPrev = (Button)findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //edtTextEmail.setError("Test");

        service = RetrofitClient.getInstance().create(ServiceApi.class);
    }

    private void attemptRegister() {
        edtTextEmail.setError(null);
        edtTextUsername.setError(null);
        edtTextPass.setError(null);

        String email = edtTextEmail.getText().toString();
        String userName = edtTextUsername.getText().toString();
        String password = edtTextPass.getText().toString();

        boolean isValidData = false;

        // 입력한 값들의 유효성 검사
        // 이메일 주소, 사용자 이름, 비밀번호: isEmpty()
        // 이메일 형식: 정규표현식 이용

        if (email.isEmpty()) {
            edtTextEmail.setError("이메일 주소를 입력해주세요.");
            isValidData = false;
        }
        else if (!email.matches("\\w+@\\w+\\.\\w+")) {
            edtTextEmail.setError("올바른 형식의 주소를 입력해주세요.");
            isValidData = false;
        }
        else
            isValidData = true;

        if (password.isEmpty()) {
            edtTextPass.setError("비밀번호를 입력해주세요.");
            isValidData = false;
        }
        if (userName.isEmpty()) {
            edtTextPass.setError("이름 또는 닉네임을 입력해주세요.");
            isValidData = false;
        }

        if (isValidData)
            startRegister(new RegisterData(email, userName, password));
    }

    private void startRegister(RegisterData data) {
        service.userRegister(data).enqueue(new Callback<RegisterResponse>() {
           @Override
           public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
               RegisterResponse result = response.body();
               int code = result.getCode();
               String message = result.getMessage();

               Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

               if (code == 200 || code == 201) {
                   finish();
               }
               else {
                   // 정상처리 외의 경우 HTTP 코드와 함께 오류 출력
                   //Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                   Toast.makeText(RegisterActivity.this, code + ": " + message, Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void onFailure(Call<RegisterResponse> call, Throwable exp) {
               Toast.makeText(RegisterActivity.this, "회원가입 오류 발생", Toast.LENGTH_SHORT).show();
               Log.e("회원가입 오류 발생", exp.getMessage());
           }
        });
    }
}

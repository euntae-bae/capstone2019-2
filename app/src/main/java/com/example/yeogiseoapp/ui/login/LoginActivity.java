package com.example.yeogiseoapp.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yeogiseoapp.HallActivity;
import com.example.yeogiseoapp.R;
import com.example.yeogiseoapp.RetrofitClient;
import com.example.yeogiseoapp.ServiceApi;
import com.example.yeogiseoapp.data.LoginData;
import com.example.yeogiseoapp.data.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //private LoginViewModel loginViewModel;
    private EditText edtTextEmail = null;
    private EditText edtTextPassword = null;
    private ServiceApi service = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        */
        service = RetrofitClient.getInstance().create(ServiceApi.class);

        edtTextEmail = findViewById(R.id.edtTextLoginEmail);
        edtTextPassword = findViewById(R.id.edtTextLoginPassword);
        //final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        /*
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful

                Intent intent = new Intent(LoginActivity.this, HallActivity.class);
                intent.putExtra("id", usernameEditText.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
         */
    }
    /*
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    */
    // 입력된 사용자 정보의 형식을 검사한다
    private void attemptLogin() {
        edtTextEmail.setError(null);
        edtTextPassword.setError(null);

        String email = edtTextEmail.getText().toString();
        String password = edtTextPassword.getText().toString();

        boolean isValidData = false;

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
            edtTextPassword.setError("비밀번호를 입력해주세요.");
            isValidData = false;
        }

        if (isValidData)
            startLogin(new LoginData(email, password));
    }

    public void startLogin(final LoginData data) {
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();
                String username = result.getUsername();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                if (code == 201) {
                    // 로그인 성공
                    Log.d("login succeed", message);
                    // SharedPreferences에 로그인 정보를 저장한다.
                    SharedPreferences sp = getSharedPreferences("loggedinData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("loggedinEmail", data.getEmail());
                    editor.putString("loggedinPassword", data.getPassword());
                    editor.putString("loggedinNickname", username);
                    editor.commit();
                    Log.d("login succeed", "email: " + data.getEmail() + "/password: " + data.getPassword());

                    Intent intent = new Intent(getApplicationContext(), HallActivity.class);
                    startActivity(intent);
                }
                else {
                    // 로그인 실패
                    Log.d("login failed", message);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 오류 발생", t.getMessage());
            }
        });
    }
    /*
    private void startRegister(RegisterData data) {
        service.userRegister(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse result = response.body();
                Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    finish();
                }
                else {
                    // 정상처리 외의 경우 HTTP 코드와 함께 오류 출력
                    //Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, result.getCode() + ": " + result.getMessage(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable exp) {
                Toast.makeText(RegisterActivity.this, "회원가입 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 오류 발생", exp.getMessage());
            }
        });
    }

     */
}

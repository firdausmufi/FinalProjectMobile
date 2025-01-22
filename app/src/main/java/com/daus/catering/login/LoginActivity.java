package com.daus.catering.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.daus.catering.R;
import com.daus.catering.main.MainActivity;
import com.daus.catering.register.RegisterActivity;
import com.daus.catering.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    MaterialButton btnLogin, btnRegister;
    TextInputEditText inputUser, inputPassword;
    LoginViewModel loginViewModel;
    String strUsername, strPassword;

    private static final String BASE_URL = "https://39ddldn0-3000.asse.devtunnels.ms/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setInitLayout();
        setInputData();
    }

    private void setInitLayout() {
        inputUser = findViewById(R.id.inputUser);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        loginViewModel = ViewModelProviders.of(LoginActivity.this).get(LoginViewModel.class);
    }

    private void setInputData() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strUsername = inputUser.getText().toString();
                strPassword = inputPassword.getText().toString();

                if (strUsername.isEmpty() || strPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Ups, Form harus diisi semua!",
                            Toast.LENGTH_LONG).show();
                } else {
                    loginUser(strUsername, strPassword);
                }
            }
        });
    }

    private void loginUser(String username, String password) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<ApiResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getToken() != null) {
                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.saveToken(apiResponse.getToken());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Username atau Password salah", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login gagal, coba lagi!", Toast.LENGTH_LONG).show();
                    Log.d("LoginActivity", "Login failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("LoginError", "Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Terjadi kesalahan, coba lagi!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class ApiResponse {
        private String message;
        private String token;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public interface ApiService {

        @retrofit2.http.POST("login")
        Call<ApiResponse> loginUser(@retrofit2.http.Body LoginRequest loginRequest);
    }
}

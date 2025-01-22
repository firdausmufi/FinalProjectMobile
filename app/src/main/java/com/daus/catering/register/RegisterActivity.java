package com.daus.catering.register;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daus.catering.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText inputEmail, inputUser, inputPassword;
    MaterialButton btnRegister;
    String strEmail, strUser, strPassword;
    private static final String BASE_URL = "https://39ddldn0-3000.asse.devtunnels.ms/"; // Ganti dengan IP jika di perangkat fisik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setInitLayout();
        setInputData();
    }

    private void setInitLayout() {
        inputEmail = findViewById(R.id.inputEmail);
        inputUser = findViewById(R.id.inputUser);
        inputPassword = findViewById(R.id.inputPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setInputData() {
        btnRegister.setOnClickListener(v -> {
            strEmail = inputEmail.getText().toString();
            strUser = inputUser.getText().toString();
            strPassword = inputPassword.getText().toString();

            Log.d("RegisterActivity", "Input Email: " + strEmail);
            Log.d("RegisterActivity", "Input Username: " + strUser);
            Log.d("RegisterActivity", "Input Password: " + strPassword);

            if (strEmail.isEmpty() || strUser.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Ups, Form harus diisi semua!",
                        Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity", "Form validation failed: One or more fields are empty.");
            } else {
                registerUser(strEmail, strUser, strPassword);
            }
        });
    }

    private void registerUser(String email, String username, String password) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // Set connection timeout
                .writeTimeout(30, TimeUnit.SECONDS)    // Set write timeout
                .readTimeout(30, TimeUnit.SECONDS)     // Set read timeout
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)  // Use custom OkHttp client
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        RegisterRequest registerRequest = new RegisterRequest(email, username, password);

        Log.d("RegisterActivity", "Sending register request with email: " + email);
        Call<ApiResponse> call = apiService.registerUser(registerRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("RegisterActivity", "API Response Code: " + response.code());
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getMessage().equals("User registered successfully.")) {
                        Toast.makeText(RegisterActivity.this, "Berhasil Register! Silahkan Login.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Gagal Register, coba lagi.", Toast.LENGTH_SHORT).show();
                        Log.d("RegisterActivity", "Register failed: " + (apiResponse != null ? apiResponse.getMessage() : "No message"));
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Registrasi gagal, coba lagi!", Toast.LENGTH_SHORT).show();
                    Log.d("RegisterActivity", "Registration failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Terjadi kesalahan, coba lagi!", Toast.LENGTH_SHORT).show();
                Log.e("RegisterActivity", "Error occurred while registering user", t);
            }
        });
    }

    public static class RegisterRequest {
        private String email;
        private String username;
        private String password;

        public RegisterRequest(String email, String username, String password) {
            this.email = email;
            this.username = username;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        @retrofit2.http.POST("register")
        Call<ApiResponse> registerUser(@retrofit2.http.Body RegisterRequest registerRequest);
    }
}

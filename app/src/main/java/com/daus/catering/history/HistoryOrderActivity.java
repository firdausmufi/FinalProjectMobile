package com.daus.catering.history;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daus.catering.R;
import com.daus.catering.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class HistoryOrderActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://39ddldn0-3000.asse.devtunnels.ms/"; // Ganti dengan URL API yang sesuai
    private String token;

    private List<Order> orderList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private RecyclerView rvHistory;
    private TextView tvNotFound;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);

        setToolbar();
        initViews();
        getTokenFromSession();
        fetchOrdersFromApi();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initViews() {
        rvHistory = findViewById(R.id.rvHistory);
        tvNotFound = findViewById(R.id.tvNotFound);
        tvNotFound.setVisibility(View.GONE);

        historyAdapter = new HistoryAdapter(this, orderList, order -> {
            Toast.makeText(HistoryOrderActivity.this, "Pesan ulang: " + order.getUsername(), Toast.LENGTH_SHORT).show();
        });

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(historyAdapter);
    }

    private void getTokenFromSession() {
        SessionManager sessionManager = new SessionManager(this);
        token = sessionManager.getToken();
    }

    private void fetchOrdersFromApi() {
        if (token == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<OrderResponse> call = apiService.getOrders("Bearer " + token);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body().getOrders();
                    if (orders != null && !orders.isEmpty()) {
                        orderList.clear();
                        orderList.addAll(orders);
                        historyAdapter.notifyDataSetChanged();
                        rvHistory.setVisibility(View.VISIBLE);
                        tvNotFound.setVisibility(View.GONE);
                    } else {
                        tvNotFound.setVisibility(View.VISIBLE);
                        rvHistory.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(HistoryOrderActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(HistoryOrderActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Retrofit Client setup
    public static class RetrofitClient {
        private static Retrofit retrofit;

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

    // API Service
    public interface ApiService {
        @GET("/orders")
        Call<OrderResponse> getOrders(@Header("Authorization") String token);
    }

    // Order Response Model
    public static class OrderResponse {
        private List<Order> orders;

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }
    }
}

package com.daus.catering.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.daus.catering.R;
import com.daus.catering.login.LoginActivity;
import com.daus.catering.utils.FunctionHelper;
import com.daus.catering.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class OrderActivity extends AppCompatActivity {

    public static final String DATA_TITLE = "TITLE";
    private static final String TAG = "OrderActivity";
    private SessionManager sessionManager;

    private static final String BASE_URL = "https://39ddldn0-3000.asse.devtunnels.ms/";  // Replace with your API's base URL

    String strTitle;
    int paket1 = 10500, paket2 = 34000, paket3 = 23700, paket4 = 22500, paket5 = 16500, paket6 = 26000;
    int itemCount1 = 0, itemCount2 = 0, itemCount3 = 0, itemCount4 = 0, itemCount5 = 0, itemCount6 = 0;
    int countP1, countP2, countP3, countP4, countP5, countP6, totalItems, totalPrice;
    ImageView imageAdd1, imageAdd2, imageAdd3, imageAdd4, imageAdd5, imageAdd6,
            imageMinus1, imageMinus2, imageMinus3, imageMinus4, imageMinus5, imageMinus6;
    Toolbar toolbar;
    TextView tvPaket1, tvPaket2, tvPaket3, tvPaket4, tvPaket5, tvPaket6,
            tvPaket11, tvJumlahPorsi, tvTotalPrice;
    MaterialButton btnCheckout;
    OrderViewModel orderViewModel;
    private OrderApiService orderApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        sessionManager = new SessionManager(this);

        setStatusbar();
        setInitLayout();
        setPaket1();
        setPaket2();
        setPaket3();
        setPaket4();
        setPaket5();
        setPaket6();
        setInputData();

        initRetrofit();
    }

    private void initRetrofit() {
        // Ambil token dari SessionManager
        String authToken = sessionManager.getToken();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    okhttp3.Request original = chain.request();

                    // Tambahkan header Authorization dengan token jika ada
                    okhttp3.Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + authToken)
                            .method(original.method(), original.body());

                    return chain.proceed(requestBuilder.build());
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderApiService = retrofit.create(OrderApiService.class);

        Log.d(TAG, "Retrofit initialized with BASE_URL: " + BASE_URL);
    }
    private void setInitLayout() {
        tvPaket11 = findViewById(R.id.tvPaket11);
        toolbar = findViewById(R.id.toolbar);
        tvPaket1 = findViewById(R.id.tvPaket1);
        tvPaket2 = findViewById(R.id.tvPaket2);
        tvPaket3 = findViewById(R.id.tvPaket3);
        tvPaket4 = findViewById(R.id.tvPaket4);
        tvPaket5 = findViewById(R.id.tvPaket5);
        tvPaket6 = findViewById(R.id.tvPaket6);
        tvJumlahPorsi = findViewById(R.id.tvJumlahPorsi);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        imageAdd1 = findViewById(R.id.imageAdd1);
        imageAdd2 = findViewById(R.id.imageAdd2);
        imageAdd3 = findViewById(R.id.imageAdd3);
        imageAdd4 = findViewById(R.id.imageAdd4);
        imageAdd5 = findViewById(R.id.imageAdd5);
        imageAdd6 = findViewById(R.id.imageAdd6);
        imageMinus1 = findViewById(R.id.imageMinus1);
        imageMinus2 = findViewById(R.id.imageMinus2);
        imageMinus3 = findViewById(R.id.imageMinus3);
        imageMinus4 = findViewById(R.id.imageMinus4);
        imageMinus5 = findViewById(R.id.imageMinus5);
        imageMinus6 = findViewById(R.id.imageMinus6);
        btnCheckout = findViewById(R.id.btnCheckout);

        strTitle = getIntent().getExtras() != null ? getIntent().getExtras().getString(DATA_TITLE) : "Default Title";
        if (strTitle != null) {
            setSupportActionBar(toolbar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(strTitle);
        }

        tvPaket11.setPaintFlags(tvPaket11.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        Log.d(TAG, "Layout initialized with title: " + strTitle);
    }
    private void setPaket1() {
        imageAdd1.setOnClickListener(v -> {
            itemCount1++;
            tvPaket1.setText(String.valueOf(itemCount1));
            countP1 = paket1 * itemCount1;
            setTotalPrice();
            Log.d(TAG, "Paket 1 count: " + itemCount1 + ", Total: " + countP1);
        });

        imageMinus1.setOnClickListener(v -> {
            if (itemCount1 > 0) {
                itemCount1--;
                tvPaket1.setText(String.valueOf(itemCount1));
            }
            countP1 = paket1 * itemCount1;
            setTotalPrice();
            Log.d(TAG, "Paket 1 count: " + itemCount1 + ", Total: " + countP1);
        });
    }
    private void setPaket2() {
        imageAdd2.setOnClickListener(v -> {
            itemCount2++;
            tvPaket2.setText(String.valueOf(itemCount2));
            countP2 = paket2 * itemCount2;
            setTotalPrice();
            Log.d(TAG, "Paket 2 count: " + itemCount2 + ", Total: " + countP2);
        });

        imageMinus2.setOnClickListener(v -> {
            if (itemCount2 > 0) {
                itemCount2--;
                tvPaket2.setText(String.valueOf(itemCount2));
            }
            countP2 = paket2 * itemCount2;
            setTotalPrice();
            Log.d(TAG, "Paket 2 count: " + itemCount2 + ", Total: " + countP2);
        });
    }
    private void setPaket3() {
        imageAdd3.setOnClickListener(v -> {
            itemCount3++;
            tvPaket3.setText(String.valueOf(itemCount3));
            countP3 = paket3 * itemCount3;
            setTotalPrice();
            Log.d(TAG, "Paket 3 count: " + itemCount3 + ", Total: " + countP3);
        });

        imageMinus3.setOnClickListener(v -> {
            if (itemCount3 > 0) {
                itemCount3--;
                tvPaket3.setText(String.valueOf(itemCount3));
            }
            countP3 = paket3 * itemCount3;
            setTotalPrice();
            Log.d(TAG, "Paket 3 count: " + itemCount3 + ", Total: " + countP3);
        });
    }
    private void setPaket4() {
        imageAdd4.setOnClickListener(v -> {
            itemCount4++;
            tvPaket4.setText(String.valueOf(itemCount4));
            countP4 = paket4 * itemCount4;
            setTotalPrice();
            Log.d(TAG, "Paket 4 count: " + itemCount4 + ", Total: " + countP4);
        });

        imageMinus4.setOnClickListener(v -> {
            if (itemCount4 > 0) {
                itemCount4--;
                tvPaket4.setText(String.valueOf(itemCount4));
            }
            countP4 = paket4 * itemCount4;
            setTotalPrice();
            Log.d(TAG, "Paket 4 count: " + itemCount4 + ", Total: " + countP4);
        });
    }
    private void setPaket5() {
        imageAdd5.setOnClickListener(v -> {
            itemCount5++;
            tvPaket5.setText(String.valueOf(itemCount5));
            countP5 = paket5 * itemCount5;
            setTotalPrice();
            Log.d(TAG, "Paket 5 count: " + itemCount5 + ", Total: " + countP5);
        });

        imageMinus5.setOnClickListener(v -> {
            if (itemCount5 > 0) {
                itemCount5--;
                tvPaket5.setText(String.valueOf(itemCount5));
            }
            countP5 = paket5 * itemCount5;
            setTotalPrice();
            Log.d(TAG, "Paket 5 count: " + itemCount5 + ", Total: " + countP5);
        });
    }
    private void setPaket6() {
        imageAdd6.setOnClickListener(v -> {
            itemCount6++;
            tvPaket6.setText(String.valueOf(itemCount6));
            countP6 = paket6 * itemCount6;
            setTotalPrice();
            Log.d(TAG, "Paket 6 count: " + itemCount6 + ", Total: " + countP6);
        });

        imageMinus6.setOnClickListener(v -> {
            if (itemCount6 > 0) {
                itemCount6--;
                tvPaket6.setText(String.valueOf(itemCount6));
            }
            countP6 = paket6 * itemCount6;
            setTotalPrice();
            Log.d(TAG, "Paket 6 count: " + itemCount6 + ", Total: " + countP6);
        });
    }
    private void setTotalPrice() {
        totalItems = itemCount1 + itemCount2 + itemCount3 + itemCount4 + itemCount5 + itemCount6;
        totalPrice = countP1 + countP2 + countP3 + countP4 + countP5 + countP6;

        tvJumlahPorsi.setText(String.format("%d items", totalItems));
        tvTotalPrice.setText(FunctionHelper.rupiahFormat(totalPrice));
        Log.d(TAG, "Total items: " + totalItems + ", Total price: " + totalPrice);
    }
    private void setInputData() {
        btnCheckout.setOnClickListener(v -> {
            if (totalItems == 0 || totalPrice == 0) {
                Toast.makeText(this, "Ups, pilih menu makanan dulu!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Checkout failed: No items selected.");
            } else if (totalItems < 10) {
                Toast.makeText(this, "Ups, minimal 10 pesanan!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Checkout failed: Minimum 10 items required.");
            } else {
                // Membuat list OrderItem berdasarkan data yang ada
                List<OrderItem> orderItems = new ArrayList<>();

                // Menambahkan item ke dalam list orderItems sesuai dengan jumlah yang dipilih
                if (itemCount1 > 0) {
                    orderItems.add(new OrderItem("Paket 1", itemCount1, paket1));
                }
                if (itemCount2 > 0) {
                    orderItems.add(new OrderItem("Paket 2", itemCount2, paket2));
                }
                if (itemCount3 > 0) {
                    orderItems.add(new OrderItem("Paket 3", itemCount3, paket3));
                }
                if (itemCount4 > 0) {
                    orderItems.add(new OrderItem("Paket 4", itemCount4, paket4));
                }
                if (itemCount5 > 0) {
                    orderItems.add(new OrderItem("Paket 5", itemCount5, paket5));
                }
                if (itemCount6 > 0) {
                    orderItems.add(new OrderItem("Paket 6", itemCount6, paket6));
                }

                // Membuat objek Order dengan list OrderItem
                Order order = new Order(orderItems);

                Log.d(TAG, "Placing order with items: " + orderItems.size() + " items.");

                // Mengirimkan data pesanan ke API
                placeOrder(order);
            }
        });
    }


    private void placeOrder(Order order) {
        String token = sessionManager.getToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan, silakan login ulang.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Call<OrderResponse> call = orderApiService.placeOrder("Bearer " + token, order);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Order successful: " + response.body().getMessage());
                    Toast.makeText(OrderActivity.this, "Yeay! Pesanan Anda sedang diproses, cek di menu riwayat ya!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        // Baca isi error body
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.d(TAG, "Bad request: " + errorBody);

                        if (response.code() == 400) {
                            Toast.makeText(OrderActivity.this, "Data pesanan tidak valid: " + errorBody, Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 401) {
                            Toast.makeText(OrderActivity.this, "Sesi kedaluwarsa, silakan login ulang.", Toast.LENGTH_SHORT).show();
                            sessionManager.clearSession();
                            startActivity(new Intent(OrderActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(OrderActivity.this, "Terjadi kesalahan: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                        Toast.makeText(OrderActivity.this, "Kesalahan tak terduga terjadi.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.e(TAG, "Order failed: " + t.getMessage(), t);
                Toast.makeText(OrderActivity.this, "Gagal menghubungi server.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class Order {
        private List<OrderItem> orderDetails;

        // Constructor
        public Order(List<OrderItem> orderDetails) {
            this.orderDetails = orderDetails;
        }

        // Getter dan Setter
        public List<OrderItem> getOrderDetails() {
            return orderDetails;
        }

        public void setOrderDetails(List<OrderItem> orderDetails) {
            this.orderDetails = orderDetails;
        }
    }

    public static class OrderResponse {
        private boolean success;
        private String message;

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    public class OrderItem {
        private String item;
        private int quantity;
        private double price;

        // Constructor
        public OrderItem(String item, int quantity, double price) {
            this.item = item;
            this.quantity = quantity;
            this.price = price;
        }

        // Getter dan Setter
        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    public interface OrderApiService {
        @POST("/order")
        Call<OrderResponse> placeOrder(
                @Header("Authorization") String token,
                @Body Order order
        );
    }
}

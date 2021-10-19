package com.example.cwash_pro.ui.customer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.cwash_pro.R;
import com.example.cwash_pro.adapters.NotificationAdapter;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.Notification;
import com.example.cwash_pro.models.ServerResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    List<Notification> notifications;
    RecyclerView rvNotification;
    NotificationAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Thông báo");
        toolbar.setNavigationIcon(R.drawable.backicon);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        rvNotification = findViewById(R.id.rvNotification);
        notifications = new ArrayList<>();
        RetrofitClient.getInstance().create(ApiService.class).getNotifyUser().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if (response.body().success) {
                    notifications = response.body().notifications;
                    Log.d("onResponse54: ", notifications.size() + "huhu");
                    adapter = new NotificationAdapter(NotificationActivity.this, notifications);
                    rvNotification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvNotification.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                Log.d("onFailure: ", t.getMessage());
            }
        });
    }
}
package com.example.cwash_pro.ui.customer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cwash_pro.R;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.Schedule;
import com.example.cwash_pro.models.ScheduleBody;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.models.Time;
import com.example.cwash_pro.ui.customer.fragments.AccountFragment;
import com.example.cwash_pro.ui.customer.fragments.HomeFragment;
import com.example.cwash_pro.ui.customer.fragments.MoreFunctionFragment;
import com.example.cwash_pro.services.RemindService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static int alarmHour, alarmMin;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    private boolean exit = false;
    private List<Schedule> scheduleList = new ArrayList<>();
    private Schedule mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();

        HomeFragment homeFragment = new HomeFragment();
        AccountFragment accountFragment = new AccountFragment();
        MoreFunctionFragment moreFunctionFragment = new MoreFunctionFragment();
        setCurrentFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    setCurrentFragment(homeFragment);
                    return true;
                case R.id.account:
                    setCurrentFragment(accountFragment);
                    return true;
                case R.id.more:
                    setCurrentFragment(moreFunctionFragment);
                    return true;
            }

            return false;
        });
        RetrofitClient.getInstance().create(ApiService.class).getSchedulesUser().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().schedules.size(); i++) {
                        String time = response.body().schedules.get(i).getTimeBook();
                        String hour = time.substring(0, 5);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                        try {
                            setNotificationTime(simpleDateFormat.parse(hour).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
            }
        });

    }

    private void setCurrentFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.flFragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
        }
        exit = true;
        Toast.makeText(this, "Bấm lần nữa để thoát", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> exit = false, 1500);
    }

    public void setNotificationTime(Long mScheduleBody) {
        Intent myIntent = new Intent(MainActivity.this, RemindService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, mScheduleBody, pendingIntent);
    }
}
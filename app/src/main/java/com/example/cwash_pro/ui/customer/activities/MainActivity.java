package com.example.cwash_pro.ui.customer.activities;

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
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null && response.body().success) {

                    scheduleList = response.body().schedules;
                    Log.e("longtq", scheduleList.toString() );
                }


            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
//        Intent myIntent = new Intent(MainActivity.this, RemindService.class);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 12);
//        calendar.set(Calendar.MINUTE, 00);
//        calendar.set(Calendar.SECOND, 00);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 23000, pendingIntent);
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

    public void setNotificationTime(String mScheduleBody) {
        Intent intent = new Intent(this, RemindService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Long.parseLong(mScheduleBody), pendingIntent);
    }
}
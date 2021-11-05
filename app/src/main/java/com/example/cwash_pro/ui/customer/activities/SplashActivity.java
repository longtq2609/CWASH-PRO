package com.example.cwash_pro.ui.customer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.cwash_pro.R;

import java.text.DateFormat;
import java.util.Calendar;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    TextView tvTime;
    TextView tvTimeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        hideSystemUI();
        getTime(tvTime);
        getMayDay(tvTimeDate);
        goToLoginActivity();
    }

    private void init() {
        tvTime = findViewById(R.id.timeClock);
        tvTimeDate = findViewById(R.id.timeDate);
    }

    private void getTime(TextView timeClock) {
        Calendar cal = Calendar.getInstance();
        int minutes = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Log.e("TAG", "getTime: " + minutes + " - " + hour);
        String result = hour + ":" + minutes;
        timeClock.setText(result);
    }

    private void getMayDay(TextView mayDay) {
        Calendar cal = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        mayDay.setText(currentDate);
    }

    private void goToLoginActivity() {
        new Handler().postDelayed(() -> new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }, 2000), 1000);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
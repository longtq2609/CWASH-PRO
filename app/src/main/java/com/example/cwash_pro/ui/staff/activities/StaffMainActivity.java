package com.example.cwash_pro.ui.staff.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cwash_pro.R;
import com.example.cwash_pro.ui.customer.fragments.AccountFragment;
import com.example.cwash_pro.models.Schedule;
import com.example.cwash_pro.ui.dialog.CustomDialogProgress;
import com.example.cwash_pro.ui.staff.fragments.CancelledScheduleFragment;
import com.example.cwash_pro.ui.staff.fragments.CompletedScheduleFragment;
import com.example.cwash_pro.ui.staff.fragments.PendingScheduleFragment;
import com.example.cwash_pro.ui.staff.fragments.ProcessingScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class StaffMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    List<Schedule> scheduleListPending;
    List<Schedule> scheduleListConfirm;
    List<Schedule> scheduleListComplete;
    List<Schedule> scheduleListCancel;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        scheduleListPending = new ArrayList<>();
        scheduleListConfirm = new ArrayList<>();
        scheduleListComplete = new ArrayList<>();
        scheduleListCancel = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        setCurrentFragment(new PendingScheduleFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.schedule:
                        setCurrentFragment(new PendingScheduleFragment());
                    return true;
                case R.id.perform:
                        setCurrentFragment(new ProcessingScheduleFragment());
                    return true;
                case R.id.completed:
                        setCurrentFragment(new CompletedScheduleFragment());
                    return true;
                case R.id.cancelled:
                        setCurrentFragment(new CancelledScheduleFragment());
                    return true;
                case R.id.account:
                        setCurrentFragment(new AccountFragment());
            }
            return false;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.flFragment2, fragment).commit();
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
}
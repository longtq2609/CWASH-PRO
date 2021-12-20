package com.example.cwash_pro.ui.staff.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cwash_pro.R;
import com.example.cwash_pro.adapters.ScheduleAdapter;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.Schedule;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.ui.dialog.CustomDialogProgress;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingScheduleFragment extends Fragment {
    private RecyclerView rvSchedule;
    private List<Schedule> scheduleList;
    ScheduleAdapter scheduleAdapter;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processing_schedule, container, false);
        initView(view);
        scheduleList = new ArrayList<>();
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final CustomDialogProgress dialog = new CustomDialogProgress(getContext());
        dialog.show();
        RetrofitClient.getInstance().create(ApiService.class).getAllSchedule().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                List<Schedule> schedules = null;
                if (response.body() != null) {
                    schedules = response.body().schedules;
                }
                if (response.body() != null) {
                    if (response.body().success) {
                        for (int i = 0; i < schedules.size(); i++) {
                            if (schedules.get(i).getStatus().equals("Confirmed")) {
                                scheduleList.add(schedules.get(i));
                            }
                        }
                        scheduleAdapter = new ScheduleAdapter(scheduleList, getActivity(), (view1, pos) ->{
                            final CustomDialogProgress dialogLoadBook = new CustomDialogProgress(getContext());
                            dialogLoadBook.show();
                                RetrofitClient.getInstance().create(ApiService.class).complete(scheduleList.get(pos).getId(), "Completed").enqueue(new Callback<ServerResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ServerResponse> call1, @NonNull Response<ServerResponse> response1) {
                                        if (response1.body() != null) {
                                            if (response1.body().success) {
                                                Toast.makeText(getActivity(), "Lịch đã hoàn thành", Toast.LENGTH_SHORT).show();
                                                //setCurrentFragment(new CompletedScheduleFragment());
                                            } else {
                                                Toast.makeText(getActivity(), "Khách hàng chưa lấy xe, chưa thể hoàn thành lịch", Toast.LENGTH_SHORT).show();
                                            }
                                            dialogLoadBook.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ServerResponse> call12, @NonNull Throwable t) {
                                        Log.d("onFailure: ", t.getMessage());
                                    }
                                });});
                        rvSchedule.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvSchedule.setAdapter(scheduleAdapter);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Lỗi " + response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                Log.e("onFailureStaffMain: ", t.getMessage());
            }
        });
        return view;
    }

    private void initView(View view) {
        rvSchedule = view.findViewById(R.id.rvProcessingSchedule);
    }

    private void setCurrentFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.flFragment2, fragment).commit();
    }
}
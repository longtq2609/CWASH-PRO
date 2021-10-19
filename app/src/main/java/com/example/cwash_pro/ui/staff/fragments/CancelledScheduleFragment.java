package com.example.cwash_pro.ui.staff.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cwash_pro.R;
import com.example.cwash_pro.adapters.ScheduleAdapter;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.myinterface.ItemClick;
import com.example.cwash_pro.models.Schedule;
import com.example.cwash_pro.models.ServerResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CancelledScheduleFragment extends Fragment {

    private RecyclerView rvSchedule;
    private List<Schedule> scheduleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancelled_schedule, container, false);
        initView(view);
        scheduleList = new ArrayList<>();

        RetrofitClient.getInstance().create(ApiService.class).getAllSchedule().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                List<Schedule> schedules = response.body().schedules;
                if (response.body().success) {
                    for (int i = 0; i < schedules.size(); i++) {
                        if (schedules.get(i).getStatus().equals("Cancelled")) {
                            scheduleList.add(schedules.get(i));
                        }
                    }
                    ScheduleAdapter scheduleAdapter = new ScheduleAdapter(scheduleList, getActivity(), new ItemClick() {
                        @Override
                        public void setOnItemClick(View view, int pos) {
                            AlertDialog builder = new AlertDialog.Builder(getContext()).create();
                            View dialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cancel_detail, null);
                            TextView tvName = dialog.findViewById(R.id.tvName);
                            TextView tvVehicle = dialog.findViewById(R.id.tvVehicle);
                            TextView tvLicense = dialog.findViewById(R.id.tvLicense);
                            TextView tvTime = dialog.findViewById(R.id.tvTime);
                            TextView tvStatus = dialog.findViewById(R.id.tvStatus);
                            TextView tvNote = dialog.findViewById(R.id.tvNote);
                            TextView tvClose = dialog.findViewById(R.id.tvClose);
                            tvName.setText(scheduleList.get(pos).getUser().getFullName());
                            tvVehicle.setText(scheduleList.get(pos).getVehicle().getName());
                            tvLicense.setText(scheduleList.get(pos).getVehicle().getLicense());
                            tvTime.setText(scheduleList.get(pos).getTimeBook());
                            tvStatus.setText(scheduleList.get(pos).getStatus());
                            tvNote.setText(scheduleList.get(pos).getNote());
                            tvClose.setOnClickListener(v -> {
                                builder.dismiss();
                            });
                            builder.setView(dialog);
                            builder.show();
                        }

                    });
                    rvSchedule.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rvSchedule.setAdapter(scheduleAdapter);
                } else {
                    Toast.makeText(getActivity(), "Lỗi " + response.body().message, Toast.LENGTH_SHORT).show();
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
        rvSchedule = (RecyclerView) view.findViewById(R.id.rvCancelSchedule);
    }
}
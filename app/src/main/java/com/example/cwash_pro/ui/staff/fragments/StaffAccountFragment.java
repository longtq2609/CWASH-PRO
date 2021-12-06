package com.example.cwash_pro.ui.staff.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cwash_pro.R;
import com.example.cwash_pro.ui.customer.activities.LoginActivity;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.ui.dialog.CustomDialogProgress;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffAccountFragment extends Fragment {
    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvPhone;
    private LinearLayout layoutInfo, layoutLogout;
    String linkAvatar, address;

    public StaffAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_account, container, false);
        initView(view);
        final CustomDialogProgress dialog = new CustomDialogProgress(getContext());
        dialog.show();
        RetrofitClient.getInstance().create(ApiService.class).getUserInfo().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if (response.body() != null && response.body().success) {
                    Glide.with(Objects.requireNonNull(getActivity())).load(RetrofitClient.link + response.body().user.getAvatar()).into(imgAvatar);
                    linkAvatar = RetrofitClient.link + response.body().user.getAvatar();
                    tvName.setText(response.body().user.getFullName());
                    tvPhone.setText(response.body().user.getPhoneNumber());
                    address = response.body().user.getAddress();
                    dialog.dismiss();
                }
                if (response.body() != null) {
                    Log.d("onResponse: ", response.body().message);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                Log.d("onFailure: ", t.getMessage());
            }
        });
        setOnclick();
        return view;
    }

    private void initView(View view) {
        imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        layoutInfo = (LinearLayout) view.findViewById(R.id.AccountInformation);
        layoutLogout = (LinearLayout) view.findViewById(R.id.LogOut);
    }

    private void setOnclick() {
        layoutInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), StaffDetailInfoActivity.class);
            intent.putExtra("avatar", linkAvatar);
            intent.putExtra("fullName", tvName.getText().toString().trim());
            intent.putExtra("phone", tvPhone.getText().toString().trim());
            intent.putExtra("address", address);
            startActivity(intent);
        });
        layoutLogout.setOnClickListener(v -> {
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Log out");
            dialog.show();
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getContext(), LoginActivity.class));
                dialog.dismiss();
                Objects.requireNonNull(getActivity()).finish();
            }, 2000);
        });
    }
}
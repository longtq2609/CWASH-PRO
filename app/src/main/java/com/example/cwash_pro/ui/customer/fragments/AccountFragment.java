package com.example.cwash_pro.ui.customer.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cwash_pro.R;
import com.example.cwash_pro.ui.customer.activities.ChangePasswordActivity;
import com.example.cwash_pro.ui.customer.activities.HistoryActivity;
import com.example.cwash_pro.ui.customer.activities.LoginActivity;
import com.example.cwash_pro.ui.customer.activities.NotificationActivity;
import com.example.cwash_pro.ui.customer.activities.UserDetailActivity;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.User;

public class AccountFragment extends Fragment {

    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvPhone;
    private LinearLayout layoutInfo, layoutNoti, layoutHistory, layoutLogout, layoutChangePw;
    private User user;
    String linkAvatar, address;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        Glide.with(getActivity()).load(RetrofitClient.link + RetrofitClient.user.getAvatar()).into(imgAvatar);
        tvName.setText(RetrofitClient.user.getFullName());
        tvPhone.setText(RetrofitClient.user.getPhoneNumber());
        setOnclick();
        return view;
    }

    private void initView(View view) {
        imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        layoutInfo = (LinearLayout) view.findViewById(R.id.layoutInfo);
        layoutNoti = (LinearLayout) view.findViewById(R.id.layoutNoti);
        layoutHistory = (LinearLayout) view.findViewById(R.id.layoutHistory);
        layoutLogout = (LinearLayout) view.findViewById(R.id.layoutLogout);
        layoutChangePw = view.findViewById(R.id.layoutChangePw);
    }

    private void setOnclick() {
        layoutInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UserDetailActivity.class);
            startActivity(intent);
        });
        layoutHistory.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), HistoryActivity.class));
        });
        layoutNoti.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), NotificationActivity.class));
        });
        layoutChangePw.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ChangePasswordActivity.class));
        });
        layoutLogout.setOnClickListener(v -> {
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Log out");
            dialog.show();
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getContext(), LoginActivity.class));
                dialog.dismiss();
                getActivity().finish();
            }, 2000);
        });
    }

}
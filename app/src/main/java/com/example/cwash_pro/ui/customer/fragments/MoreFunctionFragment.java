package com.example.cwash_pro.ui.customer.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cwash_pro.R;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.ui.dialog.CustomDialogProgress;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreFunctionFragment extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private RoundedImageView imgAvatar;
    private TextView tvName;
    private TextView tvPhone;
    LinearLayout tvPolicyandprivacy, tvShare, tvEvaluate, tvSupport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_function, container, false);
        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("Setting", Context.MODE_PRIVATE);
        editor = preferences.edit();
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        loadDataUserInfo();
        tvPolicyandprivacy = view.findViewById(R.id.idPolicyandprivacy);
        tvShare = view.findViewById(R.id.idShare);
        tvEvaluate = view.findViewById(R.id.idEvaluate);
        tvSupport = view.findViewById(R.id.idSupport);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        tvPolicyandprivacy.setOnClickListener(v -> {
            Intent intentToLink = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cwash-pro.blogspot.com/2021/10/chinh-sach-bao-mat-thong-tin.html"));
            startActivity(intentToLink);
        });
        tvShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=dd.video.photomaker&hl=en&gl=US";
            String shareSub = "https://play.google.com/store/apps/details?id=dd.video.photomaker&hl=en&gl=US";
            intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, "Share"));
        });
        tvSupport.setOnClickListener(v -> {
            AlertDialog.Builder builderSP = new AlertDialog.Builder(getContext());
            View viewSP = LayoutInflater.from(getContext()).inflate(R.layout.dialog_support, null);
            builderSP.setView(viewSP);
            Button imvBtnCall = viewSP.findViewById(R.id.imvBtnCall);
            Button imageView = viewSP.findViewById(R.id.sendSupport);
            imageView.setOnClickListener(v1 -> {
                AlertDialog.Builder builderSendMessage = new AlertDialog.Builder(getContext());
                View viewMessage = LayoutInflater.from(getContext()).inflate(R.layout.dialog_send_message_sp, null);
                builderSendMessage.setView(viewMessage);
                builderSendMessage.create();

                builderSendMessage.setPositiveButton("Gửi", (dialog, which) -> {

                });
                builderSendMessage.setNegativeButton("Quay lại", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builderSendMessage.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);
                dialog.show();
            });
            imvBtnCall.setOnClickListener(v12 -> {
                Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0389127389"));
                startActivity(intent1);
            });
            AlertDialog dialog = builderSP.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);
            dialog.show();
        });
        tvEvaluate.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + Objects.requireNonNull(getContext()).getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + Objects.requireNonNull(getContext()).getPackageName())));
            }
        });
        return view;
    }

    private void loadDataUserInfo() {
        final CustomDialogProgress dialog = new CustomDialogProgress(getContext());
        dialog.show();
        RetrofitClient.getInstance().create(ApiService.class).getUserInfo().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if (response.body() != null && response.body().success) {
                    if (getActivity() != null) {
                        Glide.with(getActivity()).load(RetrofitClient.link + response.body().user.getAvatar()).into(imgAvatar);
                    }
                    tvName.setText(response.body().user.getFullName());
                    tvPhone.setText(response.body().user.getPhoneNumber());
                    RetrofitClient.user = response.body().user;
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                Log.d("onFailure: ", t.getMessage());
            }
        });
    }
}
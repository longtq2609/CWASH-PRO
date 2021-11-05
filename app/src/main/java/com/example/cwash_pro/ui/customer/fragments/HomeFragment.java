package com.example.cwash_pro.ui.customer.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cwash_pro.CustomDialogProgress;
import com.example.cwash_pro.R;
import com.example.cwash_pro.ui.customer.activities.CarWashServiceActivity;
import com.example.cwash_pro.ui.customer.activities.HistoryActivity;
import com.example.cwash_pro.ui.customer.activities.NotificationActivity;
import com.example.cwash_pro.ui.customer.activities.VehicleActivity;
import com.example.cwash_pro.adapters.NewsAdapter;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.myinterface.ItemClick;
import com.example.cwash_pro.models.News;
import com.example.cwash_pro.models.ServerResponse;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ImageView imgAvatar, imgNotification;
    private TextView tvName;
    private TextView tvPhone;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<News> newsList = new ArrayList<>();
    LinearLayout layoutOpenWashService, layoutOpenVehicle, layoutOpenHistory;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mViewPager2.getCurrentItem();
            if (currentPosition == newsList.size() - 1) {
                mViewPager2.setCurrentItem(0);
            } else {
                mViewPager2.setCurrentItem(currentPosition + 1);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        loadDataUserInfo();
        getNews();
        layoutOpenWashService.setOnClickListener(v -> startActivity(new Intent(getContext(), CarWashServiceActivity.class)));
        layoutOpenHistory.setOnClickListener(v -> startActivity(new Intent(getContext(), HistoryActivity.class)));
        layoutOpenVehicle.setOnClickListener(v -> startActivity(new Intent(getContext(), VehicleActivity.class)));
        imgNotification.setOnClickListener(v -> startActivity(new Intent(getContext(), NotificationActivity.class)));
        return view;
    }

    private void getNews() {
        RetrofitClient.getInstance().create(ApiService.class).getNews().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if (response.body().success) {
                    newsList = response.body().newsList;
                    Log.e("onResponse: ", String.valueOf(newsList.size()));
                    mViewPager2.setAdapter(new NewsAdapter(getContext(), newsList, (view, pos) -> {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_news_detail, null, false);
                        ImageView imgNews = v.findViewById(R.id.imgNews);
                        TextView tvTitle = v.findViewById(R.id.tvTitle);
                        TextView tvDescription = v.findViewById(R.id.tvDescription);
                        Glide.with(getActivity()).load(RetrofitClient.link + newsList.get(pos).getImage()).into(imgNews);
                        tvTitle.setText(newsList.get(pos).getTitle());
                        tvDescription.setText(newsList.get(pos).getDescription());
                        alertDialog.setView(v);
                        alertDialog.show();
                    }));
                    mCircleIndicator3.setViewPager(mViewPager2);
                    mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            mHandler.removeCallbacks(mRunnable);
                            mHandler.postDelayed(mRunnable, 3000);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                Log.e("onFailureGetNews: ", t.getMessage());
            }
        });
    }

    private void initView(View view) {
        imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        imgNotification = (ImageView) view.findViewById(R.id.imgNotification);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        layoutOpenWashService = view.findViewById(R.id.layoutOpenWashService);
        layoutOpenVehicle = view.findViewById(R.id.layoutOpenVehicle);
        layoutOpenHistory = view.findViewById(R.id.layoutOpenHistory);
        mViewPager2 = view.findViewById(R.id.view_page_2);
        mCircleIndicator3 = view.findViewById(R.id.hu);
        mViewPager2.setOffscreenPageLimit(3);
        mViewPager2.setClipToPadding(false);
        mViewPager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        mViewPager2.setPageTransformer(compositePageTransformer);
    }

    private void loadDataUserInfo() {
        RetrofitClient.getInstance().create(ApiService.class).getUserInfo().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if (response.body().success) {
                    Glide.with(getActivity()).load(RetrofitClient.link + response.body().user.getAvatar()).into(imgAvatar);
                    tvName.setText(response.body().user.getFullName());
                    tvPhone.setText(response.body().user.getPhoneNumber());
                    RetrofitClient.user = response.body().user;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                Log.d("onFailure: ", t.getMessage());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 3000);
    }
}
package com.example.cwash_pro.ui.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cwash_pro.R;
import com.example.cwash_pro.adapters.ChooseTimeAdapter;
import com.example.cwash_pro.adapters.ChooseVehicleAdapter;
import com.example.cwash_pro.adapters.ServiceAdapter;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.myinterface.ItemClick;
import com.example.cwash_pro.models.Schedule;
import com.example.cwash_pro.models.ScheduleBody;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.models.Service;
import com.example.cwash_pro.models.Time;
import com.example.cwash_pro.models.User;
import com.example.cwash_pro.models.Vehicle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarWashServiceActivity extends AppCompatActivity {
    List<Vehicle> vehicleList;
    List<Service> serviceList;
    private Spinner spnVehicleCar;
    private TextView tvDate;
    LinearLayout tvChooseDate;
    private RecyclerView rvTime, rvService;
    private Button btnBook;
    private List<Time> timeListOfCar;
    private List<Time> timeListOfMoto;
    private List<Time> timeList;
    private String dateBook = "", timeBook, vehicle;
    ServiceAdapter serviceAdapter;
    List<Schedule> schedules;
    List<Schedule> schedulesPending;
    List<User> staffList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_service);
        initView();
        vehicleList = new ArrayList<>();
        serviceList = new ArrayList<>();
        timeList = new ArrayList<>();
        timeListOfCar = new ArrayList<>();
        timeListOfMoto = new ArrayList<>();
        schedules = new ArrayList<>();
        schedulesPending = new ArrayList<>();
        staffList = new ArrayList<>();
        getVehicle();
        getServices();
        getTimeOfMoto();
        getTimeOfCar();
        timeList = timeListOfMoto;
        getStatusSchedulePending();
        rvTime.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.HORIZONTAL, false));
        tvChooseDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(CarWashServiceActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                tvDate.setText("Ngày " + dayOfMonth + " tháng " + (monthOfYear + 1) + " năm " + year1);
                dateBook = dayOfMonth + "/" + (monthOfYear + 1);
                rvTime.setAdapter(new ChooseTimeAdapter(this, timeList, dateBook, staffList, schedulesPending, (v1, pos) -> timeBook = timeList.get(pos).getTime())
                );
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
        spnVehicleCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicle = vehicleList.get(position).getId();
                if (vehicleList.get(position).getType().equals("Motorcycle")) {
                    Log.e( "onItemSelected: ","Motorcycle");
                    timeList = timeListOfMoto;
                    rvTime.setAdapter(chooseTimeAdapter());
                } else if (vehicleList.get(position).getType().equals("Car")) {
                    Log.e( "onItemSelected: ","Car");
                    timeList = timeListOfCar;
                    rvTime.setAdapter(chooseTimeAdapter());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//        chooseTimeAdapter =
        rvTime.setAdapter(chooseTimeAdapter());
        btnBook.setOnClickListener(v ->

        {
            List<Service> serviceSelect = new ArrayList<>();
            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).isChecked()) {
                    serviceSelect.add(serviceList.get(i));
                }
            }
            if (vehicle == null) {
                Toast.makeText(CarWashServiceActivity.this, "Bạn chưa chọn xe", Toast.LENGTH_SHORT).show();
            } else if (dateBook == null) {
                Toast.makeText(CarWashServiceActivity.this, "Bạn chưa chọn thời gian", Toast.LENGTH_SHORT).show();
            } else if (timeBook == null) {
                Toast.makeText(CarWashServiceActivity.this, "Bạn chưa chọn thời gian", Toast.LENGTH_SHORT).show();
            } else if (serviceSelect.size() == 0) {
                Toast.makeText(CarWashServiceActivity.this, "Bạn chưa chọn dịch vụ", Toast.LENGTH_SHORT).show();
            } else {
                ScheduleBody scheduleBody = new ScheduleBody();
                scheduleBody.setTimeBook(timeBook + " @ " + dateBook);
                scheduleBody.setVehicle(vehicle);
                scheduleBody.setServices(serviceSelect);
                RetrofitClient.getInstance().create(ApiService.class).book(scheduleBody).enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        AlertDialog builder = new AlertDialog.Builder(CarWashServiceActivity.this).create();
                        View dialog;
                        if (response.body().success) {
                            dialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_success, null);
                            Button btnClose = dialog.findViewById(R.id.btnClose);
                            LinearLayout rootView = dialog.findViewById(R.id.rootView);
                            LottieAnimationView lottieAnimationView = rootView.findViewById(R.id.lottieAnimation);
                            lottieAnimationView.setAnimation("done-animation.json");
                            lottieAnimationView.playAnimation();
                            builder.getWindow().setBackgroundDrawableResource(R.drawable.custom_shape_dialog);
                            btnClose.setOnClickListener(v -> {
                                builder.dismiss();
                                finish();
                            });
                        } else {
                            dialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_failed, null);
                            Button btnClose = dialog.findViewById(R.id.btnClose);
                            TextView tvMessage = dialog.findViewById(R.id.tvMessage);
                            tvMessage.setText(response.body().message);
                            LinearLayout rootView = dialog.findViewById(R.id.rootView);
                            LottieAnimationView lottieAnimationView = rootView.findViewById(R.id.lottieAnimation);
                            lottieAnimationView.setAnimation("fail-animation.json");
                            lottieAnimationView.playAnimation();
                            builder.getWindow().setBackgroundDrawableResource(R.drawable.custom_shape_dialog);
                            btnClose.setOnClickListener(v -> {
                                builder.dismiss();
                                finish();
                            });
                        }
                        builder.setView(dialog);
                        builder.show();
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Log.e("onFailure: ", t.getMessage());
                    }
                });
            }
        });
    }

    private void getStatusSchedulePending() {
        RetrofitClient.getInstance().create(ApiService.class).getSchedulePending().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body().success) {
                    schedulesPending = response.body().schedules;
                    staffList = response.body().users;
                    Log.e("TAG", "onResponse: " + schedulesPending.size());
                } else {
                    Toast.makeText(CarWashServiceActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("onFailureGetNumber: ", t.getMessage());
            }
        });
    }


    private void initView() {
        spnVehicleCar = findViewById(R.id.spnVehicleCar);
        tvChooseDate = findViewById(R.id.tvChooseDate);
        tvDate = findViewById(R.id.tvDate);
        rvTime = findViewById(R.id.rvTime);
        rvService = findViewById(R.id.rvService);
        btnBook = findViewById(R.id.btnBook);
    }



    private void getServices() {
        RetrofitClient.getInstance().create(ApiService.class).getServices().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body().success) {
                    serviceList = response.body().services;
                    Log.e("onResponse: ", String.valueOf(serviceList.size()));
                    serviceAdapter = new ServiceAdapter(CarWashServiceActivity.this, serviceList);
                    Log.e("onCreate: ", String.valueOf(serviceList.size()));
                    rvService.setLayoutManager(new LinearLayoutManager(CarWashServiceActivity.this));
                    rvService.setAdapter(serviceAdapter);
                } else {
                    Toast.makeText(CarWashServiceActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
            }
        });
    }

    private void getVehicle() {
        RetrofitClient.getInstance().create(ApiService.class).getVehicle().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body().success) {
                    vehicleList = response.body().vehicles;
                    ChooseVehicleAdapter chooseVehicleAdapter = new ChooseVehicleAdapter(CarWashServiceActivity.this, vehicleList);
                    spnVehicleCar.setAdapter(chooseVehicleAdapter);
                } else {
                    Toast.makeText(CarWashServiceActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("getVehicles: ", t.getMessage());
            }
        });
    }

    public void getTimeOfMoto() {
        for (int i = 8; i <= 19; i++) {
            for (int j = 0; j < 60; j = j + 30) {
                String s = null;
                if (i < 10) {
                    if (j < 10) {
                        s = i + ":" + j + "0";
                    } else {
                        s = i + ":" + j;
                    }
                } else {
                    if (j < 10) {
                        s = i + ":" + j + "0";
                    } else {
                        s = i + ":" + j;
                    }
                }
                timeListOfMoto.add(new Time(s, false, true));
            }
        }
    }

    public void getTimeOfCar() {
        for (int i = 8; i <= 19; i++) {
            for (int j = 0; j < 60; j = j + 60) {
                String s = null;
                if (i < 10) {
                    if (j < 10) {
                        s = i + ":" + j + "0";
                    } else {
                        s = i + ":" + j;
                    }
                } else {
                    if (j < 10) {
                        s = i + ":" + j + "0";
                    } else {
                        s = i + ":" + j;
                    }
                }
                timeListOfCar.add(new Time(s, false, true));
            }
        }
    }

    private ChooseTimeAdapter chooseTimeAdapter() {
        return new ChooseTimeAdapter(this, timeList, dateBook, staffList, schedulesPending, new ItemClick() {
            @Override
            public void setOnItemClick(View v, int pos) {
                timeBook = timeList.get(pos).getTime();
            }
        });
    }

}
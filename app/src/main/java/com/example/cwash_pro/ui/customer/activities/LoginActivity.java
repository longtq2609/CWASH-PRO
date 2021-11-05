package com.example.cwash_pro.ui.customer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cwash_pro.CustomDialogProgress;
import com.example.cwash_pro.R;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.ui.staff.activities.StaffMainActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtPhoneNumber, edtPassword;
    ImageView imgBack;
    TextView tvCreateAccount;
    SharedPreferences sharedPreferences;
    CheckBox cbSavePassword;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("Fetching", task.getException());
                return;
            }
            RetrofitClient.tokenDevice = task.getResult();
        });
        imgBack.setOnClickListener(v -> onBackPressed());
        btnLogin.setOnClickListener(v -> login());
        tvCreateAccount.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE);
        edtPhoneNumber.setText(sharedPreferences.getString("phoneNumber", ""));
        edtPhoneNumber.setSelection(sharedPreferences.getString("phoneNumber", "").length());
        edtPassword.setText(sharedPreferences.getString("password", ""));
        cbSavePassword.setChecked(sharedPreferences.getBoolean("remember", false));
        cbSavePassword.setOnClickListener(v -> checkCB());
    }

    private void initView() {
        btnLogin = findViewById(R.id.btnLogin);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPassword = findViewById(R.id.edtPassword);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        imgBack = findViewById(R.id.imgBack);
        cbSavePassword = findViewById(R.id.cbSavePassword);
    }

    private void login() {
        btnLogin.setEnabled(false);
        final CustomDialogProgress dialog = new CustomDialogProgress(this);
        dialog.show();
        RetrofitClient.getInstance().create(ApiService.class).login("+84" + edtPhoneNumber.getText().toString().trim(), edtPassword.getText().toString().trim(), RetrofitClient.tokenDevice).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {

                if (response.body() != null) {
                    if (response.body().success) {
                        RetrofitClient.user = response.body().user;
                        RetrofitClient.JWT = response.body().token;
                        Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        if (cbSavePassword.isChecked()) {
                            editor = sharedPreferences.edit();
                            editor.putString("phoneNumber", edtPhoneNumber.getText().toString().trim());
                            editor.putString("password", edtPassword.getText().toString().trim());
                            editor.putBoolean("remember", true);
                            editor.apply();
                        }
                        if (response.body().user.getRole().equals("Customer")) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            dialog.dismiss();
                        } else if (response.body().user.getRole().equals("Staff")) {
                            startActivity(new Intent(LoginActivity.this, StaffMainActivity.class));
                            dialog.dismiss();
                        }
                        finish();
                    } else {
                        btnLogin.setEnabled(true);
                        dialog.dismiss();
                        openDialogLoginFall(response.body().message);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                btnLogin.setEnabled(true);
                Log.e("onFailure", t.toString());
            }
        });
    }

    private void checkCB() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String phoneNumber = edtPhoneNumber.getText().toString();
        String password = edtPassword.getText().toString();
        boolean checked = cbSavePassword.isChecked();
        if (!checked) {
            editor.clear();
        } else {
            editor.putString("phoneNumber", phoneNumber);
            editor.putString("password", password);
            editor.putBoolean("save", true);
        }
        editor.apply();
    }

    private void openDialogLoginFall(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog_login, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        TextView tvTitle = alertLayout.findViewById(R.id.tvTitleDialog);
        Button btnCancelDialog = alertLayout.findViewById(R.id.btnCancelDialog);
        tvTitle.setText(message);
        alert.setView(alertLayout);
        AlertDialog dialog = alert.create();
        btnCancelDialog.setOnClickListener(view -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_shape_dialog);
        alert.setCancelable(false);
        dialog.show();
    }
}
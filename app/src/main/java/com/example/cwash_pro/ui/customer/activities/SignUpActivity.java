package com.example.cwash_pro.ui.customer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cwash_pro.R;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText edtPhoneNumber, edtPassword, edtFullName, edtAddress;
    Button btnNext;
    Button btnLogin;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        imgBack.setOnClickListener(v -> onBackPressed());
        btnNext.setOnClickListener(v -> registerAccount());
        btnLogin.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void initView() {
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullName = findViewById(R.id.edtFullName);
        edtAddress = findViewById(R.id.edtAddress);
        btnNext = findViewById(R.id.btnNext);
        btnLogin = findViewById(R.id.btnLogin);
        imgBack = findViewById(R.id.imgBack);
    }

    private void registerAccount() {
        if (!edtPhoneNumber.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty() && !edtFullName.getText().toString().isEmpty() && !edtAddress.getText().toString().isEmpty()) {
            Intent intent = new Intent(SignUpActivity.this, VerifyPhoneActivity.class);
            String phone_number = "+84" + edtPhoneNumber.getText().toString().trim();
            intent.putExtra("phone_number", phone_number);
            intent.putExtra("name", edtFullName.getText().toString());
            intent.putExtra("password", edtPassword.getText().toString().trim());
            intent.putExtra("address", edtAddress.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }
}
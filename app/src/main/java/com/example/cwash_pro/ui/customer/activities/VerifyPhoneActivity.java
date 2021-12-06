package com.example.cwash_pro.ui.customer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cwash_pro.R;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.ui.dialog.CustomDialogProgress;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneActivity extends AppCompatActivity {
    private String verificationId;
    TextInputEditText edtCode;
    FirebaseAuth firebaseAuth;
    Button btnSignUp;
    String phoneNumber;
    TextView tvNumberPhoneUser;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        edtCode = findViewById(R.id.edtCode);
        imgBack = findViewById(R.id.imgBack);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvNumberPhoneUser = findViewById(R.id.tvNumberPhoneUser);
        imgBack.setOnClickListener(v -> onBackPressed());
        firebaseAuth = FirebaseAuth.getInstance();
        phoneNumber = getIntent().getStringExtra("phone_number");
        tvNumberPhoneUser.setText(phoneNumber);
        sendVerificationCode(phoneNumber);
        btnSignUp.setOnClickListener(v -> {
            String code = edtCode.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                edtCode.setError("Enter code...");
                edtCode.requestFocus();
            }
            verifyCode(code);
        });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String phoneNumber = getIntent().getStringExtra("phone_number");
                String name = getIntent().getStringExtra("name");
                String address = getIntent().getStringExtra("address");
                String passWord = getIntent().getStringExtra("password");
                final CustomDialogProgress dialogLoadBook = new CustomDialogProgress(this);
                dialogLoadBook.show();
                RetrofitClient.getInstance().create(ApiService.class).registersUser(name, phoneNumber, address, passWord).enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(VerifyPhoneActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            final CustomDialogProgress dialogLoadBook = new CustomDialogProgress(VerifyPhoneActivity.this);
                            dialogLoadBook.show();
                            RetrofitClient.getInstance().create(ApiService.class).login(phoneNumber, passWord, RetrofitClient.tokenDevice).enqueue(new Callback<ServerResponse>() {
                                @Override
                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                    if (response.body().success) {
                                        RetrofitClient.JWT = response.body().token;
                                        RetrofitClient.user = response.body().user;
                                        startActivity(new Intent(VerifyPhoneActivity.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(VerifyPhoneActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    }
                                    dialogLoadBook.dismiss();
                                }

                                @Override
                                public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                                    Log.e("onFailure1: ", t.getMessage());
                                }
                            });
                            dialogLoadBook.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                        Log.e("onFailure2: ", t.getMessage());
                    }
                });
            } else {
                Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                edtCode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            e.printStackTrace();
        }
    };
}
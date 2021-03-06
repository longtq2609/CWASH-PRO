package com.example.cwash_pro.ui.customer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cwash_pro.R;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.ui.dialog.CustomDialogProgress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edtCurPw, edtNewPw, edtNewPwRs;
    ImageView imgBack;
    Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();
        imgBack.setOnClickListener(v -> onBackPressed());
        btnChange.setOnClickListener(v -> {
            if(!edtCurPw.getText().toString().trim().isEmpty() && !edtNewPwRs.getText().toString().trim().isEmpty() && !edtNewPw.getText().toString().trim().isEmpty()){
                if (edtNewPwRs.getText().toString().trim().equals(edtNewPw.getText().toString().trim())) {
                    final CustomDialogProgress dialog = new CustomDialogProgress(this);
                    dialog.show();
                    RetrofitClient.getInstance().create(ApiService.class).changePassword(edtCurPw.getText().toString().trim(), edtNewPw.getText().toString().trim()).enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                            if (response.body().success) {
                                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                        @Override
                        public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                            Log.d("onFailureChangePw: ", t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới không đúng", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(ChangePasswordActivity.this, "Mời nhập thông tin", Toast.LENGTH_SHORT).show();

            }


        });
    }

    private void initViews() {
        edtCurPw = findViewById(R.id.edtCurPw);
        edtNewPw = findViewById(R.id.edtNewPw);
        edtNewPwRs = findViewById(R.id.edtNewPwRs);
        btnChange = findViewById(R.id.btnChange);
        imgBack = findViewById(R.id.imgBack);
    }

}
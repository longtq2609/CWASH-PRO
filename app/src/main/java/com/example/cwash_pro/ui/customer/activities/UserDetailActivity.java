package com.example.cwash_pro.ui.customer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cwash_pro.R;
import com.example.cwash_pro.apis.ApiService;
import com.example.cwash_pro.apis.RetrofitClient;
import com.example.cwash_pro.models.ServerResponse;
import com.example.cwash_pro.ui.dialog.CustomDialogProgress;
import com.example.cwash_pro.utils.Support;
import com.github.nikartm.button.FitButton;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailActivity extends AppCompatActivity {
    private EditText editFullName;
    private EditText editPhoneNumber;
    private EditText editAddress;
    private ImageView imgAvatar, icImage, icCamera;
    Button btnUpdate;
    int REQUEST_CODE_IMAGE = 100;
    int REQUEST_CODE_IMAGE_STORAGE = 200;
    public static final int PERMISSION_READ = 0;
    Bitmap bitmap;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initView();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Back");
        }

        editPhoneNumber.setText(RetrofitClient.user.getPhoneNumber());
        editFullName.setText(RetrofitClient.user.getFullName());
        editAddress.setText(RetrofitClient.user.getAddress());
        Glide.with(Objects.requireNonNull(getApplicationContext())).load(RetrofitClient.link + RetrofitClient.user.getAvatar()).into(imgAvatar);
        checkPermission();
        btnUpdate.setOnClickListener(v -> {
            updateInfor();
            Log.d("onCreate: ", "hihi");
        });
        imgAvatar.setOnClickListener(view -> {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.dialog_chose_avatar, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(UserDetailActivity.this);
            FitButton icCamera = alertLayout.findViewById(R.id.icCamera);
            FitButton icImage = alertLayout.findViewById(R.id.icImage);
            icCamera.setOnClickListener(v -> capturePicture());
            icImage.setOnClickListener(v -> {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_CODE_IMAGE_STORAGE);
            });            alert.setView(alertLayout);
            AlertDialog dialog = alert.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);
            alert.setCancelable(false);
            dialog.show();
        });
    }

    public void updateInfor() {
        MultipartBody.Part filePart = null;
        if (uri != null) {
            Log.d("updateInfor: ", "Co anh");
            File file = new File(Support.getPathFromUri(getApplicationContext(), uri));
            RequestBody requestBody = RequestBody.create(MediaType.parse(
                    UserDetailActivity.this.getContentResolver().getType(uri)), file);
            filePart = MultipartBody.Part.createFormData(
                    "avatar", file.getName(), requestBody);
        }
        final CustomDialogProgress dialog = new CustomDialogProgress(this);
        dialog.show();
        RetrofitClient.getInstance().create(ApiService.class).updateInfo( editFullName.getText().toString(), editAddress.getText().toString(),
                filePart).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if (response.body() != null) {
                    if (response.body().success) {
                        Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_SHORT).show();
                        RetrofitClient.user = response.body().user;
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                Log.d("onFailureUser: ", t.getMessage());
            }
        });
    }

    private void initView() {
        editFullName = (EditText) findViewById(R.id.edit_full_name);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editAddress = (EditText) findViewById(R.id.edtAddress);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        icImage = findViewById(R.id.icImage);
        btnUpdate = findViewById(R.id.btnUpdate);
        icCamera = findViewById(R.id.icCamera);
    }

    private void capturePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_IMAGE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            //xử lí h.ả lúc chụp
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                imgAvatar.setImageBitmap(bitmap);
                uri = data.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_IMAGE_STORAGE && resultCode == RESULT_OK && data != null) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                uri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imgAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_READ) {
            if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
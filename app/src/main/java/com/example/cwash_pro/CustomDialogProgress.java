package com.example.cwash_pro;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class CustomDialogProgress extends Dialog {
    public CustomDialogProgress(Context context) {
        super(context);
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(windowManager);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(false);
        setOnCancelListener(null);
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_progress_dialog, null);
        setContentView(view);
    }
}

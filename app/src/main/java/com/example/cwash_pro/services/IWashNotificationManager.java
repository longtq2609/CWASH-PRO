package com.example.cwash_pro.services;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.cwash_pro.R;
import com.example.cwash_pro.ui.customer.activities.SplashActivity;
import com.example.cwash_pro.utils.Constants;

public class IWashNotificationManager {
    private final Context mCtx;
    private static IWashNotificationManager mInstance;

    private IWashNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized IWashNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new IWashNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(body);
        Intent resultIntent = new Intent(mCtx, SplashActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);

        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }
}

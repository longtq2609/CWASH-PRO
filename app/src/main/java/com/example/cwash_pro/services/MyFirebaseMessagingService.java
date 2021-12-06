package com.example.cwash_pro.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cwash_pro.utils.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("onNewToken: ", s);
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("From: ", remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.w("Message data payload: ", String.valueOf(remoteMessage.getData()));
        }
        if (remoteMessage.getNotification() != null) {
            Log.d("Message Notification Body: ", remoteMessage.getNotification().getBody());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
        Log.e("longtq", "onMessageReceived: "+remoteMessage.getData().get("message") );
        IWashNotificationManager.getInstance(this).displayNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
    }
}

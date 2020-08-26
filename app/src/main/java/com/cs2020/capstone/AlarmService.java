package com.cs2020.capstone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class AlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    NotificationManager notificationManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Foreground 에서 실행되면 Notification 을 보여줘야 됨
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default",
                        "기본 채널",
                        NotificationManager.IMPORTANCE_DEFAULT));
            }
        }

        //알림창 호출
        Log.d("AlarmService", "Alarm");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        stopSelf();
        return START_NOT_STICKY;
    }

}

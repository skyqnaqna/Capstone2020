package com.cs2020.capstone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context,AddActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "default");
        // API26(Oreo) 버전 이후는 background에서 실행 금지, Foreground에서 실행
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            context.startForegroundService(notificationIntent);
            String channelName = "매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            } else {
                context.startService(notificationIntent);
                builder.setSmallIcon(R.mipmap.ic_launcher);
            }
        }
        long[] vibrate = {0, 1000, 2000, 3000};//진동
        Uri ring = RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)//어플 아이콘 넣기
                .setContentTitle("알람")
                .setContentText("유통기한이 얼마 남지 않은 제품이 있습니다.")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setColor(Color.RED)
                .setSound(ring)
                .setVibrate(vibrate)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}



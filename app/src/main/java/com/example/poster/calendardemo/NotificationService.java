package com.example.poster.calendardemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by POSTER on 12.04.2017.
 */

public class NotificationService extends Service{
    private static final int NOTIFY_ID = 101;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("alert");
            setNotification(context);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(broadcastReceiver, new IntentFilter("wakeup"));
        return super.onStartCommand(intent, flags, startId);
    }

    private void setNotification(Context context){
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_sms_black_24dp)
                .setTicker("text")
                .setContentTitle("Message!")
                .setContentText("^.^")
                .setAutoCancel(true);
        Notification notification = builder.getNotification();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}


package com.example.call_block;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

public class CallBlockingService extends Service
{
    private final BroadcastReceiver receiver = new PhoneStateReceiver();
    private SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        registerReceiver(receiver, new IntentFilter("android.intent.action.PHONE_STATE"));

        String CHANNEL_ID = "CALL_BLOCK";
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Call Blocking Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getString(R.string.call_blocking_is_active))
                .setSmallIcon(R.drawable.call_block_round)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.app_name))
                .setActions()
                .build();

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
        manager.notify(1, notification);
        startForeground(startId, notification);
        preferences.edit().putBoolean("serviceRunning", true).apply();

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
        preferences.edit().putBoolean("serviceRunning", false).apply();
    }
}

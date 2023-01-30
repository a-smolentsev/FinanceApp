package com.add.finance;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

// Класс ServiceStartReciver необходим для запуска приёмника смс в фоновом режиме

public class ServiceStartReciver extends Service {


   SmsReceiver SmsRecv; // Инициализируем смс приёмник

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // если версия андройд больше или равна  Oreo (8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startMyOwnForeground(); // показать уведомление о запуске приложения в фоновом режиме(обязательно)
        }


       // регистрация BroadcastReceiver(широковещательного приемника)
        SmsRecv = new SmsReceiver();
        IntentFilter receiverFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver( SmsRecv, receiverFilter );


        return START_STICKY;
    }


    // уведомление о запуске приложения в фоновом режиме(обязательно) если версия андройд больше или равна  Oreo (8.0)
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){

        String NOTIFICATION_CHANNEL_ID = "com.add.finance";
        String channelName = "Sms Background Service";
        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        }
        assert chan != null;
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)

                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(7, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            // отменить регистрацию BroadcastReceiver
            unregisterReceiver(  SmsRecv );
        }
        catch (final Exception exception) {

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // остановить уведомление фоновой службы
            stopForeground(true);
        }

           // остановить  фоновую службу
            stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

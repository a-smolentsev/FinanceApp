package com.add.finance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

// Класс RestartPhone необходим для запуска приёмника смс в фоновом режиме после (перезагрузки,отключении) устройства.

public class RestartPhone extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // если версия андройд больше или равна  Oreo (8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // запуск сервиса (ServiceStartReciver)
            Intent startIntentphone = new Intent(context, ServiceStartReciver.class);
            context.startForegroundService(startIntentphone);
        }
        // если версия андройд меньше Oreo (8.0)
        else{

            Intent serviceIntent = new Intent(context,ServiceStartReciver.class);
            context.startService(serviceIntent);
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }
}

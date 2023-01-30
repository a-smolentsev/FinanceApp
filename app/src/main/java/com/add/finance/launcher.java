package com.add.finance;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class launcher extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSION_READ_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//***************************************************************** НАЧАЛО **************************************************************************


        // если версия андройд больше или равна   Marshmallow (6.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionSms(); // вызов метода запроса разрешение для смс
        }

    }



    // Запрос разрешения на чтение смс
    public void  requestPermissionSms() {

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                },
                REQUEST_CODE_PERMISSION_READ_SMS);
    }

     // Обратный вызов для результата от запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_SMS && grantResults.length == 2) {

            // если пользователь дал разрешение на чтение смс
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                // если сервис (ServiceStartReciver) не запущен
                if (!Servicerun(ServiceStartReciver.class)) {

                    // если версия андройд больше или равна  Oreo (8.0)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        // запуск сервиса (ServiceStartReciver)
                        Intent StartIntentService = new Intent(this, ServiceStartReciver.class);
                        startForegroundService(StartIntentService);

                    }
                    // если версия андройд меньше Oreo (8.0)
                    else{
                        // запуск сервиса (ServiceStartReciver)
                        Intent serviceIntent = new Intent(getApplication(),ServiceStartReciver.class);
                        startService(serviceIntent);
                    }
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // Метод проверки запущен ли сервис
    private boolean Servicerun(Class<?>  serviceClass) {
        ActivityManager manager = (ActivityManager)this. getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ( serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


//***************************************************************** КОНЕЦ **************************************************************************

    public void onClick_dohod (View view){
        Intent intent = new Intent(this, add.class);
        startActivity(intent);
    }
    public void onClick_rashod (View view){
        Intent intent = new Intent(this, add_rashod.class);
        startActivity(intent);
    }





}

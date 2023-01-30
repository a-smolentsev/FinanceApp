package com.add.finance;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


public class СheckStartActivity extends Application implements Application.ActivityLifecycleCallbacks {

  private String nameactivity;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);
    }

    public String NameActivity() {

        return  nameactivity ;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        // разделяем строку через разграничители(точка)
        String strTxt[] = String.valueOf(activity).split("\\.");
        try {
            nameactivity =strTxt[2];
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
package com.example.teamproject;

import android.app.Application;
import android.content.Context;

public class TeamProjectApplication extends Application {
    private Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationContext = this.getApplicationContext();
        ScheduleSharePrefernceManager.getInstance().init(applicationContext);
    }
}

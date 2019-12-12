package com.example.teamproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleSharePrefernceManager {

    private Context applicationContext;
    public static final String PREFS_NAME = "MyPrefs";
    public static final String CALENDER = "Calender";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private ScheduleSharePrefernceManager() {
    }

    private static class Holder {
        private static final ScheduleSharePrefernceManager fileManger = new ScheduleSharePrefernceManager();
    }

    public void init(Context applicationContext) {
        this.applicationContext = applicationContext.getApplicationContext();
        sharedPreferences = applicationContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        gson = new Gson();
    }

    public static ScheduleSharePrefernceManager getInstance() {
        return Holder.fileManger;
    }

    public CSchdule getSchedule() {
        String schedule = sharedPreferences.getString(CALENDER, "");
        CSchdule gcalendar;
        if (schedule.isEmpty()) {
            gcalendar = new CSchdule();
            editSchedule(gcalendar);
        }
        gcalendar = gson.fromJson(schedule, CSchdule.class);
        if (gcalendar == null) return new CSchdule();
        return gcalendar;
    }

    public void editSchedule(CSchdule cSchdule) {
        if (cSchdule != null) {
            sharedPreferences.edit().remove(CALENDER).apply();
            sharedPreferences.edit().putString(CALENDER, gson.toJson(cSchdule)).apply();
        }
    }
}

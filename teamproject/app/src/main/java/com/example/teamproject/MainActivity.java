package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teamproject.Calendar.FragmentCalender;
import com.example.teamproject.Checklist.FragmentChecklist;
import com.example.teamproject.Timetable.FragmentTimetable;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTimetable fragmentTimetable = new FragmentTimetable();
    private FragmentCalender fragmentCalendar = new FragmentCalender();
    private FragmentChecklist fragmentChecklist = new FragmentChecklist();
    private FragmentSetting fragmentSetting = new FragmentSetting();
    private BottomNavigationView bottomNavigationItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentTimetable).commitAllowingStateLoss();

        bottomNavigationItemView = findViewById(R.id.navigationView);
        bottomNavigationItemView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int request = requestCode & 0xffff;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        fragment.onActivityResult(request, resultCode, data);
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.TimetableItem:
                    transaction.replace(R.id.frameLayout, fragmentTimetable).commitAllowingStateLoss();
                    break;
                case R.id.CalendarItem:
                    transaction.replace(R.id.frameLayout, fragmentCalendar).commitAllowingStateLoss();
                    break;
                case R.id.ChecklistItem:
                    transaction.replace(R.id.frameLayout, fragmentChecklist).commitAllowingStateLoss();
                    break;
                case R.id.SettingItem:
                    transaction.replace(R.id.frameLayout, fragmentSetting).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
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

    static final String idCalendar = "CALENDAR";
    static final String idChecklist = "CHECKLIST";
    static final String idSetting = "SETTING";
    static final String idTimetable = "TIMETABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.add(R.id.frameLayout, fragmentCalendar, idCalendar);
        transaction.add(R.id.frameLayout, fragmentChecklist, idChecklist);
        transaction.add(R.id.frameLayout, fragmentSetting, idSetting);
        transaction.add(R.id.frameLayout, fragmentTimetable, idTimetable);
        transaction.commit();

        bottomNavigationItemView = findViewById(R.id.navigationView);
        bottomNavigationItemView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int request = requestCode & 0xffff;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        fragment.onActivityResult(request, resultCode, data);
        fragmentChecklist.onActivityResult(FragmentChecklist.CHECKLIST_ADD_ITEM_REQUEST, resultCode, data);
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment target;
            switch(menuItem.getItemId())
            {
                case R.id.TimetableItem:
                    target = fragmentManager.findFragmentByTag(idTimetable);
                    if(target != null) {
                        fragmentManager.beginTransaction().show(target).commit();
                        fragmentManager.beginTransaction().hide(fragmentSetting).commit();
                        fragmentManager.beginTransaction().hide(fragmentChecklist).commit();
                        fragmentManager.beginTransaction().hide(fragmentCalendar).commit();
                    }
                    break;

                case R.id.CalendarItem:
                    target = fragmentManager.findFragmentByTag(idCalendar);
                    if(target != null) {
                        fragmentManager.beginTransaction().show(target).commit();
                        fragmentManager.beginTransaction().hide(fragmentSetting).commit();
                        fragmentManager.beginTransaction().hide(fragmentTimetable).commit();
                        fragmentManager.beginTransaction().hide(fragmentChecklist).commit();
                    }
                    break;

                case R.id.ChecklistItem:
                    target = fragmentManager.findFragmentByTag(idChecklist);
                    if(target != null) {
                        fragmentManager.beginTransaction().show(target).commit();
                        fragmentManager.beginTransaction().hide(fragmentSetting).commit();
                        fragmentManager.beginTransaction().hide(fragmentTimetable).commit();
                        fragmentManager.beginTransaction().hide(fragmentCalendar).commit();
                    }
                    break;

                case R.id.SettingItem:
                    target = fragmentManager.findFragmentByTag(idSetting);
                    if(target != null) {
                        fragmentManager.beginTransaction().show(target).commit();
                        fragmentManager.beginTransaction().hide(fragmentTimetable);
                        fragmentManager.beginTransaction().hide(fragmentChecklist);
                        fragmentManager.beginTransaction().hide(fragmentCalendar);
                    }
                    break;
            } // https://medium.com/sweet-bytes/switching-between-fragments-without-the-mindless-killing-spree-9efee5f51924
            return true;
        }
    }
}
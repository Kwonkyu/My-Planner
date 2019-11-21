package com.example.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTimetable fragmentTimetable = new FragmentTimetable();
    private FragmentCalendar fragmentCalendar = new FragmentCalendar();
    private FragmentChecklist fragmentChecklist = new FragmentChecklist();
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

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.searchItem:
                    transaction.replace(R.id.frameLayout, fragmentTimetable).commitAllowingStateLoss();

                    break;
                case R.id.cameraItem:
                    transaction.replace(R.id.frameLayout, fragmentCalendar).commitAllowingStateLoss();
                    break;
                case R.id.callItem:
                    transaction.replace(R.id.frameLayout, fragmentChecklist).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
package com.example.teamproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teamproject.Timetable.FragmentTimetable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTimetable fragmentTimetable = new FragmentTimetable();
    private com.example.teamproject.FragmentCalendar fragmentCalendar = new com.example.teamproject.FragmentCalendar();
    private FragmentChecklist fragmentChecklist = new FragmentChecklist();
    private FragmentSetting fragmentSetting = new FragmentSetting();
    private BottomNavigationView bottomNavigationItemView;

    public Spinner spinner;
    public ArrayList<String> spinnerArray;
    public ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 생성
        Toolbar tb = (Toolbar) findViewById(R.id.add_timetable_toolbar) ;
        setSupportActionBar(tb) ;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentTimetable).commitAllowingStateLoss();
        setActionBar("시간표");

        bottomNavigationItemView = findViewById(R.id.navigationView);
        bottomNavigationItemView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    public void setActionBar(String title) {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);
        spinner = findViewById(R.id.timetable_spinner);
        if(title.equals("시간표")) {
            spinner.setVisibility(View.VISIBLE);
            spinnerArray =  new ArrayList<String>();

            String path = getFilesDir().getAbsolutePath();
            File file = new File(path);
            File[] fileList = file.listFiles();
            Arrays.sort(fileList, new Comparator<Object>()
                    {
                        @Override
                        public int compare(Object object1, Object object2) {
                            String s1 = ((File)object1).lastModified()+"";
                            String s2 = ((File)object2).lastModified()+"";
                            return s1.compareTo(s2);
                        }
                    });

            if(fileList.length != 0) {
                for(File f : fileList) {
                    if(f.getName().substring(0,10).equals("timetable_")) spinnerArray.add(f.getName().substring(10,f.getName().length()-4));
                }
            }
            if(spinnerArray.size() == 0) spinnerArray.add("시간표 1");

            adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else {
            spinner.setVisibility(View.INVISIBLE);
        }
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
                    setActionBar("시간표");
                    break;
                case R.id.CalendarItem:
                    transaction.replace(R.id.frameLayout, fragmentCalendar).commitAllowingStateLoss();
                    setActionBar("캘린더");
                    break;
                case R.id.ChecklistItem:
                    transaction.replace(R.id.frameLayout, fragmentChecklist).commitAllowingStateLoss();
                    setActionBar("체크리스트");
                    break;
                case R.id.SettingItem:
                    transaction.replace(R.id.frameLayout, fragmentSetting).commitAllowingStateLoss();
                    setActionBar("설정");
                    break;
            }
            return true;
        }
    }
}
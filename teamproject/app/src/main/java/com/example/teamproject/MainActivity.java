package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teamproject.Calendar.FragmentCalender;
import com.example.teamproject.Checklist.FragmentChecklist;
import com.example.teamproject.Timetable.FragmentTimetable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTimetable fragmentTimetable = new FragmentTimetable();
    private FragmentCalender fragmentCalendar = new FragmentCalender();
    private FragmentChecklist fragmentChecklist = new FragmentChecklist();
    private BottomNavigationView bottomNavigationItemView;

    public Spinner spinner;
    public ArrayList<String> spinnerArray;
    public ArrayAdapter<String> adapter;

    static final String idCalendar = "CALENDAR";
    static final String idChecklist = "CHECKLIST";
    static final String idTimetable = "TIMETABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 생성
        Toolbar tb = (Toolbar) findViewById(R.id.add_timetable_toolbar);
        setSupportActionBar(tb) ;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setActionBar("시간표");

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.add(R.id.frameLayout, fragmentCalendar, idCalendar);
        transaction.add(R.id.frameLayout, fragmentChecklist, idChecklist);
        transaction.add(R.id.frameLayout, fragmentTimetable, idTimetable);
        transaction.commit();
        fragmentManager.beginTransaction().show(fragmentTimetable).commit();
        fragmentManager.beginTransaction().hide(fragmentChecklist).commit();
        fragmentManager.beginTransaction().hide(fragmentCalendar).commit();
        bottomNavigationItemView = findViewById(R.id.navigationView);
        bottomNavigationItemView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        int command = getIntent().getIntExtra("COMMAND", -1);
        if(command > 0){
            fragmentManager.beginTransaction().hide(fragmentTimetable).commit();
            fragmentManager.beginTransaction().hide(fragmentSetting).commit();
            fragmentManager.beginTransaction().show(fragmentChecklist).commit();
            fragmentManager.beginTransaction().hide(fragmentCalendar).commit();
            bottomNavigationItemView.getMenu().getItem(2).setChecked(true);
        }
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
        if(request == 1 || request == 2) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            fragment.onActivityResult(request, resultCode, data);
        }
        else if(request == 1000) {
            fragmentChecklist.onActivityResult(request, resultCode, data);
        }


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
                        fragmentManager.beginTransaction().hide(fragmentChecklist).commit();
                        fragmentManager.beginTransaction().hide(fragmentCalendar).commit();
                        setActionBar("시간표");
                    }
                    break;

                case R.id.CalendarItem:
                    target = fragmentManager.findFragmentByTag(idCalendar);
                    if(target != null) {
                        fragmentManager.beginTransaction().show(target).commit();
                        fragmentManager.beginTransaction().hide(fragmentTimetable).commit();
                        fragmentManager.beginTransaction().hide(fragmentChecklist).commit();
                        setActionBar("캘린더");
                    }
                    break;

                case R.id.ChecklistItem:
                    target = fragmentManager.findFragmentByTag(idChecklist);
                    if(target != null) {
                        fragmentManager.beginTransaction().show(target).commit();
                        fragmentManager.beginTransaction().hide(fragmentTimetable).commit();
                        fragmentManager.beginTransaction().hide(fragmentCalendar).commit();
                        setActionBar("체크리스트");
                    }
                    break;

            } // https://medium.com/sweet-bytes/switching-between-fragments-without-the-mindless-killing-spree-9efee5f51924
            return true;
        }
    }
}
package com.example.teamproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.teamproject.CSchdule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class FragmentCalendar extends Fragment implements CalendarListAdapter.RecyclerviewClick{
    public static final String PREFS_NAME = "MyPrefs";

    private Gson gson;

    static final int CALENDARLIST_ADD_ITEM_REQUEST = 10;

    private CalendarView mCalendarView;
    private TextView date_TextView;
    private TextView date_work;
    private TextView date_startT;
    private TextView date_endT;

    public static final int CHECKLIST_ADD_ITEM_REQUEST = 1000;
    private RecyclerView calendarlist_view;
    private CalendarListAdapter calendarlist_adapter;
    private RecyclerView.LayoutManager calendarlist_manager;
    private ArrayList<String> calendarlist;
    private ArrayList<String> flist;
    private CSchdule cschdule;

    String date;
    String get_data;
    String get_data2;
    String FILENAME;
    String te;
    String selected_date;

    Calendar start_cal;
    Calendar end_cal;

    MovableFloatingActionButton floatingbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        cschdule = new CSchdule();
        start_cal = Calendar.getInstance();
        end_cal = Calendar.getInstance();
        gson = new GsonBuilder().create();


    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendarlist_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendarlist_menu_removeall:
                try {
                    String lists = "";        // list 의 값들을 이어붙여 저장할 변수 lists
                    Log.d("FragmentCalendar_remove", "removing...");
                    FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(lists.getBytes());    // lists 의 값을 저장
                    fos.close();                    // 열었던 파일을 닫음
                } catch (IOException e) {       //파일 관련 작업 시의 예외처리
                    e.printStackTrace();
                }
                read_file(FILENAME);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mCalendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        date_TextView = (TextView) view.findViewById(R.id.calendar_date);
        date_work = view.findViewById(R.id.calendar_testcase);
        date_startT = view.findViewById(R.id.calendar_startT);
        date_endT = view.findViewById(R.id.calendar_endT);

        floatingbtn = view.findViewById(R.id.fab);

        floatingbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), CalendarListItemAdd.class);
                startActivityForResult(intent, CALENDARLIST_ADD_ITEM_REQUEST);
            }
        });

        flist = new ArrayList<>();
        calendarlist = new ArrayList<>();
        calendarlist_view = view.findViewById(R.id.calendar_recycler_list);
        calendarlist_view.setHasFixedSize(true);
        calendarlist_manager = new LinearLayoutManager(getActivity());
        calendarlist_view.setLayoutManager(calendarlist_manager);
        calendarlist_adapter = new CalendarListAdapter(calendarlist);
        calendarlist_view.setAdapter(calendarlist_adapter);
        calendarlist_adapter.setRecyclerviewClick(this);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        int month = cal.get(cal.MONTH) + 1;
        int dates = cal.get(cal.DATE);
        date = year + "/" + month + "/" + dates;
        date_TextView.setText(date);
        FILENAME = year + "_" + month + "_" + dates + ".txt";

        try {
            calendarlist.clear();
            FileInputStream fis = getActivity().openFileInput(FILENAME);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            te = new String(buffer);        // 읽은 값을 te 에 저장
            String[] array = te.split("/"); // 배열 array 에 "/" 가 나올때마다 잘라서 삽입
            for (int i = 0; i < array.length; i++) {
                calendarlist.add(array[i]);
                calendarlist_adapter.notifyDataSetChanged();
                Log.d("FragmentCalendar", array[i]);
            }
            fis.close();                        // 열었던 파일을 닫음
        } catch (IOException e) {
            e.printStackTrace();                // 파일 관련 작업 시의 예외처리
            calendarlist.clear();
            calendarlist_adapter.notifyDataSetChanged();
        }

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                date = year + "/" + (month + 1) + "/" + dayOfMonth;
                date_TextView.setText(date);
                FILENAME = year + "_" + (month + 1) + "_" + dayOfMonth + ".txt";
                calendarlist.clear();
                read_file(FILENAME);

                selected_date= year + "_" + (month + 1) + "_" + dayOfMonth;
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  // 다른 클래스의 인텐트를 통해 받아온 값에 대한 함수
        if (requestCode == CALENDARLIST_ADD_ITEM_REQUEST) {     // requestCode 가 GET_STRING 인 경우
            if (resultCode == RESULT_OK) {
                get_data = data.getStringExtra("ADD_TITLE");
                get_data2 = data.getStringExtra("ADD_WORK");

                int starty = data.getIntExtra("ADD_START_Y",0);
                int startmo = data.getIntExtra("ADD_START_Mo",0);
                int startd = data.getIntExtra("ADD_START_D",0);
                int starth = data.getIntExtra("ADD_START_H",0);
                int startm = data.getIntExtra("ADD_START_M",0);

                int endy = data.getIntExtra("ADD_END_Y",0);
                int endmo = data.getIntExtra("ADD_END_Mo",0);
                int endd = data.getIntExtra("ADD_END_D",0);
                int endh = data.getIntExtra("ADD_END_H",0);
                int endm = data.getIntExtra("ADD_END_M",0);

                start_cal.set(Calendar.YEAR, starty);
                start_cal.set(Calendar.MONTH, startmo);
                start_cal.set(Calendar.DAY_OF_MONTH, startd);
                start_cal.set(Calendar.HOUR, starth);
                start_cal.set(Calendar.MINUTE, startm);

                end_cal.set(Calendar.YEAR, endy);
                end_cal.set(Calendar.MONTH, endmo);
                end_cal.set(Calendar.DAY_OF_MONTH, endd);
                end_cal.set(Calendar.HOUR, endh);
                end_cal.set(Calendar.MINUTE,endm);

                cschdule.setTitle(get_data);
                cschdule.setWork(get_data2);
                cschdule.setStartTIme(start_cal);
                cschdule.setEndTime(end_cal);

                FILENAME = starty + "_" + startmo  + "_" + startd + ".txt";
                selected_date= starty + "_" + startmo  + "_" + startd;


/*
                Log.d("FragmentCalendar_title", cschdule.getTitle());
                Log.d("FragmentCalendar_work", cschdule.getWork());
                Log.d("FragmentCalendar_sTY", ""+cschdule.getStartTimeYear());
                Log.d("FragmentCalendar_sTMo", ""+cschdule.getStartTimeMonth());
                Log.d("FragmentCalendar_sTD", ""+cschdule.getStartTImeDay());
                Log.d("FragmentCalendar_sTH", ""+cschdule.getStartTimeHour());
                Log.d("FragmentCalendar_sTM", ""+cschdule.getStartTimeMinute());

                Log.d("FragmentCalendar_eTY", ""+cschdule.getEndTimeYear());
                Log.d("FragmentCalendar_eTMo", ""+cschdule.getEndTimeMonth());
                Log.d("FragmentCalendar_eTD", ""+cschdule.getEndTImeDay());
                Log.d("FragmentCalendar_eTH", ""+cschdule.getEndTimeHour());
                Log.d("FragmentCalendar_eTM", ""+cschdule.getEndTimeMinute());
*/
                try {
                    FileInputStream fis = getActivity().openFileInput(FILENAME);
                    flist.clear();
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    te = new String(buffer);        // 읽은 값을 te 에 저장
                    String[] array = te.split("/"); // 배열 array 에 "/" 가 나올때마다 잘라서 삽입
                    Log.d("FragmentCalendar_bfread", te);
                    if (te != null && !te.isEmpty())
                        for (int i = 0; i < array.length; i++) {
                            flist.add(array[i]);
                            calendarlist_adapter.notifyDataSetChanged();
                            Log.d("FragmentCalendar_add", array[i]);
                        }
                    fis.close();                        // 열었던 파일을 닫음

                } catch (IOException e) {
                    e.printStackTrace();                // 파일 관련 작업 시의 예외처리
                    calendarlist.clear();
                    calendarlist_adapter.notifyDataSetChanged();
                }


                flist.add(get_data);
                //save_file(FILENAME);
                try {
                    String lists = "";        // list 의 값들을 이어붙여 저장할 변수 lists
                    for (int i = 0; i < flist.size(); i++) {
                        if (flist.get(0) == "") {
                            Log.d("FragmentCalendar_chk", "check");
                        } else {
                            lists = lists + flist.get(i) + "/";    // 항목마다 "/" 을 사이에 넣어가며 lists 에 list 값을 저장함
                            Log.d("FragmentCalendar_save", lists);
                            Log.d("FragmentCalendar_save2", flist.get(i));
                        }
                    }
                    Log.d("FragmentCalendar_svlist", lists);
                    FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(lists.getBytes());    // lists 의 값을 저장
                    fos.close();                    // 열었던 파일을 닫음
                } catch (IOException e) {       //파일 관련 작업 시의 예외처리
                    e.printStackTrace();
                }

              // read_file(FILENAME);

                try {
                    calendarlist.clear();
                    flist.clear();
                    FileInputStream fis = getActivity().openFileInput(FILENAME);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    te = new String(buffer);        // 읽은 값을 te 에 저장
                    String[] array = te.split("/"); // 배열 array 에 "/" 가 나올때마다 잘라서 삽입
                    for (int i = 0; i < array.length; i++) {
                        calendarlist.add(array[i]);
                        calendarlist_adapter.notifyDataSetChanged();
                        Log.d("FragmentCalendar_read", array[i]);
                    }
                    fis.close();                        // 열었던 파일을 닫음
                } catch (IOException e) {
                    e.printStackTrace();                // 파일 관련 작업 시의 예외처리
                    calendarlist.clear();
                    calendarlist_adapter.notifyDataSetChanged();
                }
                String strSchedule = gson.toJson(cschdule, CSchdule.class);
                String inputdata = get_data + "_" + selected_date;

                SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(inputdata, strSchedule);
                editor.commit();
            }
        }
    }

    private void read_file(String filename){
        try {
            calendarlist.clear();
            FileInputStream fis = getActivity().openFileInput(filename);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            te = new String(buffer);        // 읽은 값을 te 에 저장
            String[] array = te.split("/"); // 배열 array 에 "/" 가 나올때마다 잘라서 삽입
            for (int i = 0; i < array.length; i++) {
                calendarlist.add(array[i]);
                calendarlist_adapter.notifyDataSetChanged();
                Log.d("FragmentCalendar_read", array[i]);
            }
            fis.close();                        // 열었던 파일을 닫음
        } catch (IOException e) {
            e.printStackTrace();                // 파일 관련 작업 시의 예외처리
            calendarlist.clear();
            calendarlist_adapter.notifyDataSetChanged();
        }
    }
    private void save_file(String filename){
        try {
            String lists = "";        // list 의 값들을 이어붙여 저장할 변수 lists
            for (int i = 0; i < flist.size(); i++) {
                if (flist.get(0) == "") {
                    Log.d("FragmentCalendar_chk", "check");
                } else {
                    lists = lists + flist.get(i) + "/";    // 항목마다 "/" 을 사이에 넣어가며 lists 에 list 값을 저장함
                    Log.d("FragmentCalendar_save", lists);
                    Log.d("FragmentCalendar_save2", flist.get(i));
                }
            }
            Log.d("FragmentCalendar_svlist", lists);
            FileOutputStream fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(lists.getBytes());    // lists 의 값을 저장
            fos.close();                    // 열었던 파일을 닫음
        } catch (IOException e) {       //파일 관련 작업 시의 예외처리
            e.printStackTrace();
        }
    }

    @Override
    public void onClickRecyclerviewClicked(int position) {
        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String inputdata = calendarlist.get(position) + "_" + selected_date;
        String strSchedule = sp.getString(inputdata, "");

        CSchdule gcalendar = gson.fromJson(strSchedule, CSchdule.class);

        date_work.setText(gcalendar.getWork());
        date_startT.setText(gcalendar.getStartTimeYear()+"/"+gcalendar.getStartTimeMonth()+"/"+gcalendar.getStartTImeDay()+"   "
                            +gcalendar.getStartTimeHour()+"시 " + gcalendar.getStartTimeMinute()+"분");
        date_endT.setText(gcalendar.getEndTimeYear()+"/"+gcalendar.getEndTimeMonth()+"/"+gcalendar.getEndTImeDay()+"   "
                            +gcalendar.getEndTimeHour()+"시 " + gcalendar.getEndTimeMinute() + "분");


    }
}


package com.example.teamproject;

import android.content.Context;
import android.content.Intent;
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

import com.example.teamproject.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class FragmentCalendar extends Fragment implements CalendarListAdapter.RecyclerviewClick{

    static final int CALENDARLIST_ADD_ITEM_REQUEST = 1;

    private CalendarView mCalendarView;
    private TextView date_TextView;

    public static final int CHECKLIST_ADD_ITEM_REQUEST = 1000;
    private RecyclerView calendarlist_view;
    private CalendarListAdapter calendarlist_adapter;
    private RecyclerView.LayoutManager calendarlist_manager;
    private ArrayList<String> calendarlist;
    private ArrayList<String> flist;
    String date;
    String get_data;
    String FILENAME;
    String te;

    private Intent mIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendarlist_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendarlist_menu_add:
                Intent intent = new Intent(getActivity(), CalendarListItemAdd.class);
                startActivityForResult(intent, CALENDARLIST_ADD_ITEM_REQUEST);
                break;
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
                //Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
                date_TextView.setText(date);
                FILENAME = year + "_" + (month + 1) + "_" + dayOfMonth + ".txt";
                calendarlist.clear();
                read_file(FILENAME);
                /*
                try {
                    calendarlist.clear();
                    FileInputStream fis = getActivity().openFileInput(FILENAME);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    te = new String(buffer);        // 읽은 값을 te 에 저장
                    Log.d("FragmentCalendar_bfread", te);
                    String[] array = te.split("/"); // 배열 array 에 "/" 가 나올때마다 잘라서 삽입
                    if (array[0] == "") Log.d("FragmentCalendar_read", "first array is null!");
                    for (int i = 0; i < array.length; i++) {

                        calendarlist.add(array[i]);
                        calendarlist_adapter.notifyDataSetChanged();
                        if (array[i] == "")
                            Log.d("FragmentCalendar_read", "first array is null!!!!");
                        Log.d("FragmentCalendar_read", array[i]);
                    }
                    fis.close();                        // 열었던 파일을 닫음
                } catch (IOException e) {
                    e.printStackTrace();                // 파일 관련 작업 시의 예외처리
                    Log.d("FragmentCalendar_excep", "excep");
                    calendarlist.clear();
                    calendarlist_adapter.notifyDataSetChanged();
                }
                 */
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  // 다른 클래스의 인텐트를 통해 받아온 값에 대한 함수
        if (requestCode == CALENDARLIST_ADD_ITEM_REQUEST) {     // requestCode 가 GET_STRING 인 경우
            if (resultCode == RESULT_OK) {
                get_data = data.getStringExtra("ADD_TEXT");
                //Toast.makeText(getActivity(), get_data, Toast.LENGTH_SHORT).show();
               // read_file(FILENAME);


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
        Toast.makeText(getActivity().getApplicationContext() , calendarlist.get(position), Toast.LENGTH_SHORT).show();
    }
}

package com.example.teamproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.teamproject.Model.Lecture;
import com.example.teamproject.Timetable.AddTimetableActivity;
import com.example.teamproject.Timetable.FragmentTimetable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class CalendarListDialog extends DialogFragment {

    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;

    private Fragment fragment;
    String strSchedule;
    String get_values;
    int get_id;


    TextView title;
    TextView work;
    TextView start_day;
    TextView end_day;
    TextView start_time;
    TextView end_time;


    public CalendarListDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calendarlist, container, false);

        helper = new DBHelper(getContext());
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        Bundle args = getArguments();

        fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout);


        // 커스텀 다이얼로그의 각 위젯들을 정의한다.

        get_values = args.getString("ID");
        int pos = get_values.indexOf("_");
        String get_title = get_values.substring(0,pos);
        int get_stt = Integer.parseInt(get_values.substring(pos+1, get_values.length()));

        Log.d("get_id",""+get_values);
        Log.d("get_title",""+get_title);
        Log.d("get_stt",""+get_stt);


        c=db.rawQuery("SELECT title, work, startday, endday, starttime, endtime, _id  FROM calendar WHERE title='"+get_title+"' AND startday = "+get_stt+";", null);
        c.moveToFirst();
        get_id = c.getInt(6);

//
        Log.d("get_val",""+c.getString(0));
        Log.d("get_val",""+c.getString(1));
        Log.d("get_val",""+c.getInt(2));
        Log.d("get_val",""+c.getInt(3));
        Log.d("get_val",""+c.getInt(4));
        Log.d("get_val",""+c.getInt(5));
        Log.d("get_val",""+get_id);
        title = view.findViewById(R.id.dialog_cltitle);
        work = view.findViewById(R.id.dialog_clwork);
        start_day = view.findViewById(R.id.dialog_clstartd);
        end_day = view.findViewById(R.id.dialog_clendd);
        start_time = view.findViewById(R.id.dialog_clstartt);
        end_time = view.findViewById(R.id.dialog_clendt);


        title.setText(c.getString(0));
        work.setText(c.getString(1));

        String st_d = value_to_date(c.getInt(2));
        String end_d = value_to_date(c.getInt(3));
        String st_t = value_to_time(c.getInt(4));
        String end_t = value_to_time(c.getInt(5));
        start_day.setText(st_d);
        end_day.setText(end_d);
        start_time.setText(st_t);
        end_time.setText(end_t);

        Button modifyBtn = view.findViewById(R.id.lecture_modify_button);
        Button deleteBtn = view.findViewById(R.id.lecture_delete_button);




        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((FragmentCalender)getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout)).deleteItem(get_id);
                } catch (Exception e) {
                    Log.d("tttt", e.getMessage());
                }
                dismiss();
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((FragmentCalender)getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout)).modifyItem(get_id);
                } catch (Exception e) {
                    Log.d("tttt", e.getMessage());
                }
                dismiss();
            }
        });
        return view;
    }

    public String value_to_date(int date){
        String values = Integer.toString(date);
        String year =  values.substring(0,4);
        String month =  values.substring(4,6);
        String day =  values.substring(6,8);
        Log.d("FragmentCalendar_year",""+year);
        Log.d("FragmentCalendar_month",""+month);
        Log.d("FragmentCalendar_day",""+day);
        Log.d("FragmentCalendar_values",""+ year+"/"+month+"/"+day);
        return year+"/"+month+"/"+day;
    }

    public String value_to_time(int time){
        String values = Integer.toString(time);
        if(values.length()<4) values = "0"+values;
        String hour =  values.substring(0,2);
        String minute =  values.substring(2,4);
        Log.d("FragmentCalendar_year",""+hour);
        Log.d("FragmentCalendar_month",""+minute);
        Log.d("FragmentCalendar_values",""+ hour+"시 "+minute+"분");
        return hour+":"+minute;
    }
}


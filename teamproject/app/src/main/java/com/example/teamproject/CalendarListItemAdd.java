package com.example.teamproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarListItemAdd extends AppCompatActivity {

    String todotitle;
    String todowork;

    //Calendar start_cal;
    //Calendar end_cal;

    int start_hour;
    int start_minute;

    int start_year;
    int start_month;
    int start_day;

    int end_hour;
    int end_minute;

    int end_year;
    int end_month;
    int end_day;

    EditText calendarlist_add;
    EditText calendarlist_add2;


    Button s_button;
    Button s_button2;
    Button e_button;
    Button e_button2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.calendarlist_item_add);
        super.onCreate(savedInstanceState);

        calendarlist_add = findViewById(R.id.calendarlist_edit_add);
        calendarlist_add2 = findViewById(R.id.calendarlist_edit_add2);

        s_button = findViewById(R.id.start_button);
        s_button2 = findViewById(R.id.start_button2);
        e_button = findViewById(R.id.end_button);
        e_button2 = findViewById(R.id.end_button2);



        s_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showStartTime();
            }
        });

        s_button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showStartDate();
            }
        });

        e_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showEndTime();
            }
        });

        e_button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showEndDate();
            }
        });

        Button addBtn = findViewById(R.id.add_button);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 버튼이 눌렸을 때 작동하는 함수
                switch(v.getId()) {
                    case R.id.add_button :
                        todotitle = calendarlist_add.getText().toString();
                        todowork = calendarlist_add2.getText().toString();
                        Intent intent = new Intent();
                        intent.putExtra("ADD_TITLE", todotitle);
                        intent.putExtra("ADD_WORK", todowork);

                        intent.putExtra("ADD_START_Y", start_year);
                        intent.putExtra("ADD_START_Mo", start_month);
                        intent.putExtra("ADD_START_D", start_day);
                        intent.putExtra("ADD_START_H", start_hour);
                        intent.putExtra("ADD_START_M", start_minute);

                        intent.putExtra("ADD_END_Y", end_year);
                        intent.putExtra("ADD_END_Mo", end_month);
                        intent.putExtra("ADD_END_D", end_day);
                        intent.putExtra("ADD_END_H", end_hour);
                        intent.putExtra("ADD_END_M", end_minute);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendarlist_additem_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.calendarlist_menu_save:
                Toast.makeText(getApplicationContext(), "Still in development", Toast.LENGTH_SHORT).show();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showStartTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                s_button.setText(hourOfDay + "시 " + minute + "분");
                start_hour = hourOfDay;
                start_minute = minute;
            }
        }, 12, 0, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }
    public void showEndTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                e_button.setText(hourOfDay + "시 " + minute + "분");
                end_hour = hourOfDay;
                end_minute = minute;

            }
        }, 13, 0, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    public void showStartDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, 2019, 10, 1 );
        datePickerDialog.show();
    }

    public void showEndDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener2, 2019, 10, 1 );
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            //Toast.makeText(getApplicationContext(), i + "년 " + i1 + "월 " + i2 +"일", Toast.LENGTH_SHORT).show();
            s_button2.setText(i + "년 " + (i1+1) + "월 " + i2 +"일");
            start_year = i;
            start_month = i1+1;
            start_day = i2;
        }
    };

    private DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            //Toast.makeText(getApplicationContext(), i + "년 " + i1 + "월 " + i2 +"일", Toast.LENGTH_SHORT).show();
            e_button2.setText(i + "년 " + (i1+1) + "월 " + i2 +"일");
            end_year = i;
            end_month = i1+1;
            end_day = i2;
        }
    };



}

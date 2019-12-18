package com.example.teamproject.Calendar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.teamproject.DBHelper;
import com.example.teamproject.R;

import java.util.Calendar;


public class CalendarListItemAdd extends AppCompatActivity {

    AlarmManager alarm_manager;
    Context context;
    Intent my_intent;
    Calendar alarm_calendar;

    String todotitle;
    String todowork;

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

    int default_sh = 12;
    int default_sm = 0;
    int default_eh = 13;
    int default_em = 0;

    int pos;
    int mod_check;

    int s_val;
    int e_val;

    EditText calendarlist_title;
    EditText calendarlist_work;

    Button s_day_btn;
    Button s_time_btn;
    Button e_day_btn;
    Button e_time_btn;

    String strat_put;
    String end_put;
    int startt_put;
    int endt_put;

    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.calendarlist_item_add);
        super.onCreate(savedInstanceState);

        this.context = this;
        alarm_calendar = Calendar.getInstance();
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        my_intent = new Intent(this.context, Alarm_Receiver.class);

        Toolbar tb = (Toolbar) findViewById(R.id.add_calendar_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        calendarlist_title = findViewById(R.id.calendarlist_edit_title);
        calendarlist_work = findViewById(R.id.calendarlist_edit_work);


        s_day_btn = findViewById(R.id.start_day_btn);
        s_time_btn = findViewById(R.id.start_time_btn);
        e_day_btn = findViewById(R.id.end_day_btn);
        e_time_btn = findViewById(R.id.end_tiem_btn);

        Intent intent = getIntent();
        mod_check = intent.getIntExtra("MOD_CHECK",0);
        pos = intent.getIntExtra("_ID",0);

        if(mod_check==0) {
            start_year = end_year = intent.getIntExtra("C_YEAR", 0);
            start_month = end_month = intent.getIntExtra("C_MONTH", 0);
            start_day = end_day = intent.getIntExtra("C_DAY", 0);
        }
        else if(mod_check==1){
            c=db.rawQuery("SELECT title, work, startday, endday, starttime, endtime, _id  FROM calendar " +
                    "WHERE _id = "+pos+"", null);
            c.moveToFirst();
            calendarlist_title.setText(c.getString(0));
            calendarlist_work.setText(c.getString(1));
            value_to_date_s(c.getInt(2));
            value_to_date_e(c.getInt(3));
            value_to_time_s(c.getInt(4));
            value_to_time_e(c.getInt(5));

            s_day_btn.setText(start_year + "년 " + start_month + "월 " + start_day + "일");
            e_day_btn.setText(end_year + "년 " + end_month + "월 " + end_day + "일");
            s_time_btn.setText(start_hour + "시 " + start_minute + "분");
            e_time_btn.setText(end_hour+ "시 " + end_minute + "분");

            default_sh = start_hour; default_sm = start_minute;
            default_eh = end_hour; default_em = end_minute;

        }

        s_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTime();
            }
        });

        s_day_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDate();
            }
        });

        e_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTime();
            }
        });

        e_day_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDate();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {  //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.add_calendar_button: {

                strat_put = String.format("%d/%02d/%02d", start_year, (start_month), start_day);
                end_put = String.format("%d/%02d/%02d", end_year, (end_month), end_day);
                startt_put = start_hour*100+start_minute;
                endt_put = end_hour*100+end_minute;

                s_val = date_to_value(strat_put);
                e_val = date_to_value(end_put);

/*
                if ((end_year < start_year)
                        || (end_year >= start_year && end_month < start_month)
                        || (end_year >= start_year && end_month >= start_month && end_day < start_day)) {
                    Toast.makeText(getApplicationContext(), "종료일이 시작일보다 빠릅니다!", Toast.LENGTH_SHORT).show();
                    break;
                }*/
                if (s_val>e_val) {
                    Toast.makeText(getApplicationContext(), "종료일이 시작일보다 빠릅니다!", Toast.LENGTH_SHORT).show();
                    break;
                }


                if ((start_hour * 100 + start_minute) > (end_hour * 100 + end_minute)) {
                    Toast.makeText(getApplicationContext(), "종료 시간이 시작 시간보다 빠릅니다!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (calendarlist_title.getText().toString().equals("") || calendarlist_work.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "제목이나 내용이 입력되지 않았습니다!", Toast.LENGTH_SHORT).show();
                    break;
                }

                Intent intent = new Intent();


                todotitle = calendarlist_title.getText().toString();
                todowork = calendarlist_work.getText().toString();




                if(mod_check == 0) insert();
                else if(mod_check == 1) update();

                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void insert() {

        String sql = "insert into calendar(title, work, startday, starttime, endday, endtime) values(?, ?, ?, ?, ?, ?)";
        Object[] params = {todotitle, todowork, s_val, startt_put , e_val,endt_put };
        db.execSQL(sql, params);//이런식으로 두번쨰 파라미터로 이런식으로 객체를 전달하면 sql문의 ?를 이 params에 있는 데이터를 물음표를 대체해준다.
    }

    public void update() {

        String sql = "update calendar set title='"+todotitle+"', work='"+todowork+"', startday="+s_val+", " +
                "starttime="+startt_put+", endday="+e_val+", endtime="+endt_put+" WHERE _id = "+pos+"" ;
        db.execSQL(sql);//이런식으로 두번쨰 파라미터로 이런식으로 객체를 전달하면 sql문의 ?를 이 params에 있는 데이터를 물음표를 대체해준다.
    }

    public void showStartTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                s_time_btn.setText(hourOfDay + "시 " + minute + "분");
                start_hour = hourOfDay;
                start_minute = minute;
            }
        }, default_sh, default_sm, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    public void showEndTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                e_time_btn.setText(hourOfDay + "시 " + minute + "분");
                end_hour = hourOfDay;
                end_minute = minute;

            }
        }, default_eh, default_em, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    public void showStartDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, start_year, start_month - 1, start_day);
        datePickerDialog.show();
    }

    public void showEndDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener2, end_year, end_month - 1, end_day);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            //Toast.makeText(getApplicationContext(), i + "년 " + i1 + "월 " + i2 +"일", Toast.LENGTH_SHORT).show();
            s_day_btn.setText(i + "년 " + (i1 + 1) + "월 " + i2 + "일");
            start_year = i;
            start_month = i1 + 1;
            start_day = i2;

        }
    };

    private DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            //Toast.makeText(getApplicationContext(), i + "년 " + i1 + "월 " + i2 +"일", Toast.LENGTH_SHORT).show();
            e_day_btn.setText(i + "년 " + (i1 + 1) + "월 " + i2 + "일");
            end_year = i;
            end_month = i1 + 1;
            end_day = i2;

        }
    };

    public int date_to_value(String date){
        int values;
        int year =  Integer.parseInt(date.substring(0,4));
        int month =  Integer.parseInt(date.substring(5,7));
        int day =  Integer.parseInt(date.substring(8,10));

        values = (year*10000)+(month*100)+day;
        return values;
    }

    public void value_to_date_s(int date){
        String values = Integer.toString(date);
        String year =  values.substring(0,4);
        String month =  values.substring(4,6);
        String day =  values.substring(6,8);

        start_year = Integer.parseInt(year);
        start_month = Integer.parseInt(month);
        start_day = Integer.parseInt(day);


    }

    public void value_to_date_e(int date){
        String values = Integer.toString(date);
        String year =  values.substring(0,4);
        String month =  values.substring(4,6);
        String day =  values.substring(6,8);

        end_year = Integer.parseInt(year);
        end_month = Integer.parseInt(month);
        end_day = Integer.parseInt(day);

    }

    public void value_to_time_s(int time){
        String values = Integer.toString(time);
        if(values.length()<4) values = "0"+values;
        String hour =  values.substring(0,2);
        String minute =  values.substring(2,4);

        start_hour = Integer.parseInt(hour);
        start_minute = Integer.parseInt(minute);

    }

    public void value_to_time_e(int time){
        String values = Integer.toString(time);
        if(values.length()<4) values = "0"+values;
        String hour =  values.substring(0,2);
        String minute =  values.substring(2,4);

        end_hour = Integer.parseInt(hour);
        end_minute = Integer.parseInt(minute);

    }


}
package com.example.teamproject.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamproject.DBHelper;
import com.example.teamproject.Model.CalendarListItem;
import com.example.teamproject.Model.CheckListItem;
import com.example.teamproject.R;
import com.example.teamproject.Views.CalendarListAdapter;
import com.example.teamproject.Views.MovableFloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class FragmentCalender extends Fragment implements CalendarListAdapter.RecyclerviewLongClick {


    Calendar alarm_calendar;
    Calendar now_calendar;

    private int alarm_hour;
    private int alarm_minute;
    private int get_starttime;
    private String alarm_title;
    private String alarm_work;
    private int now_time;
    private int now_date;
    private int now_month;
    private int now_day;


    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;

    public static final int CALENDARLIST_ADD_ITEM_REQUEST = 10;

    private CalendarView mCalendarView;

    private RecyclerView calendarlist_view;
    private CalendarListAdapter calendarlist_adapter;
    private RecyclerView.LayoutManager calendarlist_manager;
    private ArrayList<CalendarListItem> calendarlist;
    private MovableFloatingActionButton floatingbtn;
    private String currentSelectedDate = "";

    private int modi_position;
    private int requestCode=0;
    private boolean alarmSet_mode = true;

    private ArrayList<String> check;

    TextView Top_Text;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        alarm_calendar = Calendar.getInstance();
        now_calendar = Calendar.getInstance();

        int now_year = now_calendar.get(Calendar.YEAR);
        now_month = now_calendar.get(Calendar.MONTH)+1;
        now_day = now_calendar.get(Calendar.DAY_OF_MONTH);

        now_date = (now_year*10000)+(now_month*100)+now_day;

        helper = new DBHelper(getContext());
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mCalendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        Top_Text = view.findViewById(R.id.top_text);
        Top_Text.setText("일정 목록 - "+ (now_month)+"월"+now_day+"일");
        calendarlist = new ArrayList<>();
        check = new ArrayList<>();
        calendarlist_view = view.findViewById(R.id.calendar_recycler_list);
        calendarlist_view.setHasFixedSize(true);
        calendarlist_manager = new LinearLayoutManager(getActivity());
        calendarlist_view.setLayoutManager(calendarlist_manager);
        calendarlist_adapter = new CalendarListAdapter(calendarlist);
        calendarlist_view.setAdapter(calendarlist_adapter);
        calendarlist_adapter.setRecyclerviewLongClick(this);
        floatingbtn = view.findViewById(R.id.fab);
        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarListItemAdd.class);
                String[] date = getCurrentDate().split("/");
                intent.putExtra("C_YEAR", Integer.parseInt(date[0]));
                intent.putExtra("C_MONTH", Integer.parseInt(date[1]));
                intent.putExtra("C_DAY", Integer.parseInt(date[2]));
                intent.putExtra("MOD_CHECK",0);
                startActivityForResult(intent, CALENDARLIST_ADD_ITEM_REQUEST);
            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                currentSelectedDate = String.format("%d/%02d/%02d", year, (month + 1), dayOfMonth);
                Top_Text.setText("일정목록 - "+ (month+1)+"월"+dayOfMonth+"일");
                update();
                return;
            }
        });
        currentSelectedDate = getCurrentDate();
        update();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendarlist_menu, menu);
        // apply checklist menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendarlist_menu_removeall:
                remove_all();

                update();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int c_year = cal.get(cal.YEAR);
        int c_month = cal.get(cal.MONTH) + 1;
        int c_dates = cal.get(cal.DATE);
        return String.format("%d/%02d/%02d", c_year, c_month, c_dates);
    }


    @Override
    public void onClickRecyclerviewLongClicked(int position) {

        Log.d("current_position",""+position);
        Log.d("FragmentCalendar_dvalue",check.get(position));
        Bundle bundle = new Bundle();
        bundle.putString("ID", check.get(position));
        CalendarListDialog dialog = new CalendarListDialog();
        dialog.setArguments(bundle);
        dialog.show(getActivity().getSupportFragmentManager(), "1");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  // 다른 클래스의 인텐트를 통해 받아온 값에 대한 함수
        if (resultCode == RESULT_OK) {
            update();
        }
    }

    public void deleteItem(int position) {
        calendarlist.clear();
        c=db.rawQuery("DELETE FROM calendar WHERE _id = "+position+"" , null);
        Log.d("position", ""+position);
        if(c.getCount() == 0 ){
            //Toast.makeText(getContext(), "일정이 없습니다.", Toast.LENGTH_SHORT).show();
            c.close();
            calendarlist_adapter.notifyDataSetChanged();
            update();
            return;
        }
        c.close();
        calendarlist_adapter.notifyDataSetChanged();
        update();
    }

    public void modifyItem(int position) {
        calendarlist.clear();
        modi_position = position;
        Intent intent = new Intent(getActivity(), CalendarListItemAdd.class);

        intent.putExtra("_ID",position);
        intent.putExtra("MOD_CHECK",1);

        startActivityForResult(intent, CALENDARLIST_ADD_ITEM_REQUEST);
        update();
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
        c.close();
    }

    public void remove_all(){
        calendarlist.clear();
        String datavalue = Integer.toString(date_to_value(currentSelectedDate));
        c=db.rawQuery("DELETE FROM calendar WHERE startday='" + datavalue + "'", null);

        if(c.getCount() == 0 ){
            Toast.makeText(getContext(), "일정이 없습니다.", Toast.LENGTH_SHORT).show();
            c.close();
            calendarlist_adapter.notifyDataSetChanged();
            return;
        }
        c.close();
        calendarlist_adapter.notifyDataSetChanged();
    }

    public void update() {
        int now_hour = now_calendar.get(Calendar.HOUR_OF_DAY);
        int now_minute = now_calendar.get(Calendar.MINUTE);
        now_time = now_hour*100+now_minute;

        calendarlist.clear();
        check.clear();
        int datavalue = date_to_value(currentSelectedDate);

        c=db.rawQuery("SELECT _id, title,startday,starttime,work  FROM calendar " +
                "WHERE startday <="+datavalue+" AND endday>="+datavalue+" ORDER BY starttime asc", null);

        if(c.getCount() == 0 ){
            c.close();
            calendarlist_adapter.notifyDataSetChanged();
            return;
        }
        else {
            while (c.moveToNext()) {
                String id = c.getString(1) + "_" + c.getInt(2);
                if(c.getInt(2)==now_date) alarmSet_mode=true;
                calendarlist.add(new CalendarListItem(c.getString(1), c.getString(4), time_to_insertValue(c.getString(3))));
                check.add(id);
                if(alarmSet_mode) {
                    get_starttime = c.getInt(3);
                    alarm_title = c.getString(1);
                    alarm_work = c.getString(4);
                    time_to_value(c.getString(3));
                    diaryNotification();
                }
            }
            alarmSet_mode=false;
            requestCode=0;
            alarm_hour=0;
            alarm_minute=0;
        }

        c.close();
        calendarlist_adapter.notifyDataSetChanged();
    }

    void diaryNotification() {
        Log.d("now_time",""+(now_time));
        Log.d("get_starttime",""+get_starttime);
        if(get_starttime>100) get_starttime-=100;
        if(now_time<get_starttime) {
            alarm_calendar.set(Calendar.HOUR_OF_DAY, (alarm_hour-1));
            alarm_calendar.set(Calendar.MINUTE, alarm_minute);
            Log.d("alarm_hour",""+(alarm_hour-1));
            Log.d("alarm_minute",""+alarm_minute);

            Boolean dailyNotify = true; // 무조건 알람을 사용

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(getActivity(), Alarm_Receiver.class);
            alarmIntent.putExtra("ALARM_TITLE",alarm_title);
            alarmIntent.putExtra("ALARM_WORK",alarm_work);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            requestCode++;
            // 사용자가 매일 알람을 허용했다면
            if (dailyNotify) {
                if (alarmManager != null) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                    Log.d("alarm_set","setted!");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm_calendar.getTimeInMillis(), pendingIntent);
                    }
                }

            }
        }
    }

    public String time_to_insertValue(String time){
        if(time.length()<4) time = "0"+time;
        String values;
        String hour =  time.substring(0,2);
        String minute =  time.substring(2,4);
        values = hour + "시 " + minute + "분";
        return values;
    }

    public int date_to_value(String date){
        int values;
        int year =  Integer.parseInt(date.substring(0,4));
        int month =  Integer.parseInt(date.substring(5,7));
        int day =  Integer.parseInt(date.substring(8,10));
        values = (year*10000)+(month*100)+day;
        return values;
    }

    public void time_to_value(String time){
        if(time.length()<4) time = "0"+time;
        alarm_hour =  Integer.parseInt(time.substring(0,2));
        alarm_minute =  Integer.parseInt(time.substring(2,4));
    }
}

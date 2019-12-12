package com.example.teamproject.Calendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamproject.Calendar.CalendarListDialog;
import com.example.teamproject.Calendar.CalendarListItemAdd;
import com.example.teamproject.DBHelper;
import com.example.teamproject.R;
import com.example.teamproject.Views.CalendarListAdapter;
import com.example.teamproject.Views.MovableFloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class FragmentCalender extends Fragment implements CalendarListAdapter.RecyclerviewClick, CalendarListAdapter.RecyclerviewLongClick {

    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;

    public static final int CALENDARLIST_ADD_ITEM_REQUEST = 10;
    public static final int CALENDARLIST_MODIFY_ITEM_REQUEST = 11;

    private CalendarView mCalendarView;


    private RecyclerView calendarlist_view;
    private CalendarListAdapter calendarlist_adapter;
    private RecyclerView.LayoutManager calendarlist_manager;
    private ArrayList<String> calendarlist;
    private MovableFloatingActionButton floatingbtn;
    private String currentSelectedDate = "";

    private int modi_position;

    private ArrayList<String> check;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        calendarlist = new ArrayList<>();
        check = new ArrayList<>();
        calendarlist_view = view.findViewById(R.id.calendar_recycler_list);
        calendarlist_view.setHasFixedSize(true);
        calendarlist_manager = new LinearLayoutManager(getActivity());
        calendarlist_view.setLayoutManager(calendarlist_manager);
        calendarlist_adapter = new CalendarListAdapter(calendarlist);
        calendarlist_view.setAdapter(calendarlist_adapter);
        calendarlist_adapter.setRecyclerviewClick(this);
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
                Log.d("FragmentCalendar_cyear1", "" + date[0]);
                Log.d("FragmentCalendar_cmonth1", "" + date[1]);
                Log.d("FragmentCalendar_cday1", "" + date[2]);
                startActivityForResult(intent, CALENDARLIST_ADD_ITEM_REQUEST);
                update();
            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                currentSelectedDate = String.format("%d/%02d/%02d", year, (month + 1), dayOfMonth);
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
    public void onClickRecyclerviewClicked(int position) {
        return;
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
        //Toast.makeText(getContext(), "????", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        //update();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
        c.close();
    }

    public void remove_all(){
        calendarlist.clear();
        c=db.rawQuery("DELETE FROM calendar WHERE startday='" + currentSelectedDate + "'", null);

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
        calendarlist.clear();
        check.clear();
        int datavalue = date_to_value(currentSelectedDate);
        Log.d("FragmentCalendar_dvalue",currentSelectedDate);
        Log.d("FragmentCalendar_dvalue",""+datavalue);
        c=db.rawQuery("SELECT _id, title,startday  FROM calendar " +
                "WHERE startday <="+datavalue+" AND endday>="+datavalue+" ORDER BY starttime asc", null);

        if(c.getCount() == 0 ){
            //Toast.makeText(getContext(), "일정이 없습니다.", Toast.LENGTH_SHORT).show();
            c.close();
            calendarlist_adapter.notifyDataSetChanged();
            return;
        }
        else {
            while (c.moveToNext()) {
                String id = c.getString(1) + "_" + c.getInt(2);
                calendarlist.add(c.getString(1));
                check.add(id);
                Log.d("FragmentCalendar_title", c.getString(1));
                Log.d("FragmentCalendar_checkid", id);
            }
        }

        c.close();
        calendarlist_adapter.notifyDataSetChanged();
    }

    //    yyyy/mm/dd

    public int date_to_value(String date){
        int values;
        int year =  Integer.parseInt(date.substring(0,4));
        int month =  Integer.parseInt(date.substring(5,7));
        int day =  Integer.parseInt(date.substring(8,10));
        Log.d("FragmentCalendar_year",""+year);
        Log.d("FragmentCalendar_month",""+month);
        Log.d("FragmentCalendar_day",""+day);
        values = (year*10000)+(month*100)+day;
        return values;
    }
}

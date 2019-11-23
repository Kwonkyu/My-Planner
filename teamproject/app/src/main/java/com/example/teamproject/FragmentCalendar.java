package com.example.teamproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
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

import com.example.teamproject.R;

import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class FragmentCalendar extends Fragment {

    static final int CALENDARLIST_ADD_ITEM_REQUEST = 1;

    private CalendarView mCalendarView;
    private TextView date_TextView;

    public static final int CHECKLIST_ADD_ITEM_REQUEST = 1000;
    private RecyclerView calendarlist_view;
    private RecyclerView.Adapter calendarlist_adapter;
    private RecyclerView.LayoutManager calendarlist_manager;
    private ArrayList<String> calendarlist;
    String date;
    String get_data;
    String FILENAME;

    private Intent mIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.calendarlist_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.calendarlist_menu_add:
                Intent intent = new Intent(getActivity(), CalendarListItemAdd.class);
                startActivityForResult(intent, CALENDARLIST_ADD_ITEM_REQUEST);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarlist = new ArrayList<>();
        calendarlist_view = view.findViewById(R.id.calendar_recycler_list);
        calendarlist_view.setHasFixedSize(true);
        calendarlist_manager = new LinearLayoutManager(getActivity());
        calendarlist_view.setLayoutManager(calendarlist_manager);
        calendarlist_adapter = new CheckListAdapter(calendarlist);
        calendarlist_view.setAdapter(calendarlist_adapter);


        mCalendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        date_TextView = (TextView) view.findViewById(R.id.calendar_date);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        int month = cal.get(cal.MONTH)+1;
        int dates = cal.get(cal.DATE);
        date = year+"/"+month+"/"+dates;
        date_TextView.setText(date);
        FILENAME = year + "_" + month + "_" + dates + ".txt";


    mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
            registerForContextMenu(calendarView);
            date = year +"/" + (month+1) + "/" + dayOfMonth;
            Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
            date_TextView.setText(date);
            FILENAME = year + "_" + month + "_" + dayOfMonth + ".txt";
        }
    });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  // 다른 클래스의 인텐트를 통해 받아온 값에 대한 함수
        if(requestCode == CALENDARLIST_ADD_ITEM_REQUEST) {     // requestCode 가 GET_STRING 인 경우
            if(resultCode == RESULT_OK) {
                get_data = data.getStringExtra("ADD_TEXT");

                Toast.makeText(getActivity(), get_data, Toast.LENGTH_SHORT).show();
            }
        }
    }


}

package com.example.termprojectt;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class FragmentCalendar extends Fragment {

    private CalendarView mCalendarView;
    private TextView date_TextView;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        mCalendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        date_TextView = (TextView) view.findViewById(R.id.calendar_date);



    mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
            String date = year +"/" + (month+1) + "/" + dayOfMonth;
            Toast.makeText(getActivity(), "" + year + "/" +
                    (month + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
            date_TextView.setText(date);
        }
    });
        return view;
    }

}

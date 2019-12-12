package com.example.teamproject.Checklist;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teamproject.Checklist.CheckListItemAdd;
import com.example.teamproject.Model.CheckListItem;
import com.example.teamproject.R;
import com.example.teamproject.Views.CheckListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.DAYS;

public class FragmentChecklist extends Fragment {
    static final int CHECKLIST_ADD_ITEM_REQUEST = 1000;
    static final int CHECKLIST_ADD_ITEM_RESULT = 1001;
    static final String CHECKLIST_ADD_ITEM_OK = "[OK]";
    static final String CHECKLIST_ADD_ITEM_DATE = "[DATE]";
    static final String CHECKLIST_ADD_ITEM_PLACE = "[PLACE]";
    private enum RangeCategory {BEFORE, TODAY, WEEK, FAR}
    // Constant int and string used in Intent, File I/O, etc...

    private RecyclerView checklist_previous, checklist_today, checklist_week, checklist_far;
    private RecyclerView.Adapter previous_adapter, today_adapter, week_adapter, far_adapter;
    private RecyclerView.LayoutManager previous_manager, today_manager, week_manager, far_manager;
    private ArrayList<CheckListItem> previous, today, week, far;
    // Recycler related variables and data structure for storing checklist items.

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        // TODO: implement checklist load from database feature(should be ordered)
        super.onResume();
        Toast.makeText(getContext(), "onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        // TODO: implement checklist backup to database feature
        super.onStop();
        Toast.makeText(getContext(), "onStop()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.checklist_menu, menu);
        // apply checklist menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.checklist_menu_add: // call checklist item add activity
                Intent intent = new Intent(getActivity(), CheckListItemAdd.class);
                startActivityForResult(intent, CHECKLIST_ADD_ITEM_REQUEST);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case CHECKLIST_ADD_ITEM_REQUEST: // result of called activity
                switch(resultCode){
                    case CHECKLIST_ADD_ITEM_RESULT: // properly added from called activity
                        String itemText = data.getStringExtra(CHECKLIST_ADD_ITEM_OK);
                        String expireDate = data.getStringExtra(CHECKLIST_ADD_ITEM_DATE);
                        String placeText = data.getStringExtra(CHECKLIST_ADD_ITEM_PLACE);
                        // Get checklist item text and expiration date
                        Calendar cal = Calendar.getInstance();
                        String currentDate = String.format(Locale.KOREAN,"%d/%d/%d",
                                cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
                        // Parse today's date to string in format of yyyy/mm/dd using
                        switch(dateComparator(currentDate, expireDate)){
                            // Compare dates(today, expiration date), add in corresponding RecyclerView.
                            // Checklist item is instantiated as CheckListItem class.
                            case BEFORE:
                                previous.add(new CheckListItem(itemText, expireDate, placeText));
                                previous_adapter.notifyDataSetChanged();
                                break;

                            case TODAY:
                                today.add(new CheckListItem(itemText, expireDate, placeText));
                                today_adapter.notifyDataSetChanged();
                                break;

                            case WEEK:
                                week.add(new CheckListItem(itemText, expireDate, placeText));
                                week_adapter.notifyDataSetChanged();
                                break;

                            case FAR:
                                far.add(new CheckListItem(itemText, expireDate, placeText));
                                far_adapter.notifyDataSetChanged();
                                break;

                            default:
                                Toast.makeText(getContext(), "Error on adding item", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checklist, container, false);
        previous = new ArrayList<>();
        today = new ArrayList<>();
        week = new ArrayList<>();
        far = new ArrayList<>();
        // data structures for RecyclerView.

        checklist_previous = (RecyclerView)rootView.findViewById(R.id.checklist_recycler_previous);
        checklist_today = (RecyclerView)rootView.findViewById(R.id.checklist_recycler_today);
        checklist_week = (RecyclerView)rootView.findViewById(R.id.checklist_recycler_week);
        checklist_far = (RecyclerView)rootView.findViewById(R.id.checklist_recycler_far);
        // link views to variables.

        checklist_previous.setNestedScrollingEnabled(false);
        checklist_today.setNestedScrollingEnabled(false);
        checklist_week.setNestedScrollingEnabled(false);
        checklist_far.setNestedScrollingEnabled(false);
        // disable nested scroll for smooth scrolling of ScrollView.

        previous_manager = new LinearLayoutManager(getActivity());
        today_manager = new LinearLayoutManager(getActivity());
        week_manager = new LinearLayoutManager(getActivity());
        far_manager = new LinearLayoutManager(getActivity());
        // construct layout managers for each view.

        checklist_previous.setLayoutManager(previous_manager);
        checklist_today.setLayoutManager(today_manager);
        checklist_week.setLayoutManager(week_manager);
        checklist_far.setLayoutManager(far_manager);
        // apply LinearLayoutManager to each view.

        previous_adapter = new CheckListAdapter(previous);
        today_adapter = new CheckListAdapter(today);
        week_adapter = new CheckListAdapter(week);
        far_adapter = new CheckListAdapter(far);
        // construct custom Adapter

        checklist_previous.setAdapter(previous_adapter);
        checklist_today.setAdapter(today_adapter);
        checklist_week.setAdapter(week_adapter);
        checklist_far.setAdapter(far_adapter);
        // apply Adapter to each view.

        return rootView;
    }

    @TargetApi(26)
    private RangeCategory dateComparator(String current, String target){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        // DD means day of year. MM is not proper. Use symbol properly.
        // Check at https://developer.android.com/reference/java/time/format/DateTimeFormatter
        LocalDate currentDate = LocalDate.parse(current, formatter);
        LocalDate targetDate = LocalDate.parse(target, formatter);
        // Parse LocalDate instance from given date string.

        // Compare dates
        if(currentDate.isAfter(targetDate)){
            return RangeCategory.BEFORE;
        } else if(currentDate.isEqual(targetDate)){
            return RangeCategory.TODAY;
        } else if(targetDate.isAfter(currentDate)){
            long between = DAYS.between(currentDate, targetDate);
            // If period between two dates are less than a week or not.
            if(between <= 7){
                return RangeCategory.WEEK;
            } else {
                return RangeCategory.FAR;
            }
        }

        return RangeCategory.TODAY;
    }

}
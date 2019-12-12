package com.example.teamproject;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


import static android.content.Context.MODE_PRIVATE;

import static java.time.temporal.ChronoUnit.DAYS;

public class FragmentChecklist extends Fragment {
    public static final String PREFS_NAME = "MyPrefs";
    private File file;
    private Gson gson;
    static final int CHECKLIST_ADD_ITEM_REQUEST = 1000;
    static final int CHECKLIST_ADD_ITEM_RESULT = 1001;
    static final String CHECKLIST_ADD_ITEM_OK = "[OK]";
    static final String CHECKLIST_ADD_ITEM_DATE = "[DATE]";
    static final String SAVE_FILENAME = "checklist_save.txt";

    private enum RangeCategory {BEFORE, TODAY, WEEK, FAR}

    // Constant int and string used in Intent, File I/O, etc...
    private RecyclerView checklist_previous, checklist_today, checklist_week, checklist_far;
    private RecyclerView.Adapter previous_adapter, today_adapter, week_adapter, far_adapter;
    private RecyclerView.LayoutManager previous_manager, today_manager, week_manager, far_manager;
    private ArrayList<String> previous, today, week, far;
    private ArrayList<String> previous_temp, today_temp, week_temp, far_temp;
    // Recycler related variables and data structure for storing checklist items.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().create();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        // TODO: implement checklist backup file load feature
        // TODO: checklist item should be ordered and categorized by expiration date
        super.onResume();
    }

    @Override
    public void onStop() {
        // TODO: implement checklist backup file save feature
//        FileOutputStream fos = getContext().openFileOutput(SAVE_FILENAME, Context.MODE_PRIVATE);
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.checklist_menu, menu);
        // apply checklist menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.checklist_menu_add: // call checklist item add activity
                Intent intent = new Intent(getActivity(), CheckListItemAdd.class);
                startActivityForResult(intent, CHECKLIST_ADD_ITEM_REQUEST);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    /*
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch(requestCode){
                case CHECKLIST_ADD_ITEM_REQUEST: // result of called activity
                    switch(resultCode){
                        case CHECKLIST_ADD_ITEM_RESULT: // properly added from called activity
                            String itemText = data.getStringExtra(CHECKLIST_ADD_ITEM_OK);
                            String expireDate = data.getStringExtra(CHECKLIST_ADD_ITEM_DATE);
                            // Get checklist item text and expiration date
                            Calendar cal = Calendar.getInstance();
                            String currentDate = String.format(Locale.KOREAN,"%d/%d/%d",
                                    cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
                            // Parse today's date to string in format of yyyy/mm/dd using
                            switch(dateComparator(currentDate, expireDate)){
                                // Compare dates(today, expiration date), add in corresponding RecyclerView.
                                // Checklist item is instantiated as CheckListItem class.
                                case BEFORE:
                                    previous.add(new CheckListItem(itemText, expireDate));
                                    previous_adapter.notifyDataSetChanged();
                                    break;

                                case TODAY:
                                    today.add(new CheckListItem(itemText, expireDate));
                                    today_adapter.notifyDataSetChanged();
                                    break;

                                case WEEK:
                                    week.add(new CheckListItem(itemText, expireDate));
                                    week_adapter.notifyDataSetChanged();
                                    break;

                                case FAR:
                                    far.add(new CheckListItem(itemText, expireDate));
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
    */
    public void Storage() {
        Calendar cal = Calendar.getInstance();
        String currentDate = String.format(Locale.KOREAN, "%d/%d/%d",
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        try {
            File list[] = getActivity().getApplicationContext().getFilesDir().listFiles();
            for (int i = 0; i < list.length; i++) {
                String expireDate = list[i].getName().replace("_", "/").substring(0, list[i].getName().length() - 4);
                Log.d("FragmentCalendar_read_exp", expireDate);
                Log.d("FragmentCalendar_read_crt", currentDate);
                switch (dateComparator(currentDate, expireDate)) {
                    // Compare dates(today, expiration date), add in corresponding RecyclerView.
                    // Checklist item is instantiated as CheckListItem class.
                    case BEFORE:

                        break;

                    case TODAY:
                        //today.add(new CheckListItem(itemText, expireDate));
                        // today_adapter.notifyDataSetChanged();
                        break;

                    case WEEK:
                        // week.add(new CheckListItem(itemText, expireDate));
                        // week_adapter.notifyDataSetChanged();
                        break;

                    case FAR:
                        // far.add(new CheckListItem(itemText, expireDate));
                        // far_adapter.notifyDataSetChanged();
                        break;

                    default:
                        Toast.makeText(getContext(), "Error on adding item", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "저장 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            Log.e("FragmentChecklist", "Storage: ",e );

        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Storage();
        View rootView = inflater.inflate(R.layout.fragment_checklist, container, false);
        previous = new ArrayList<>();
        previous_temp = new ArrayList<>();
        today = new ArrayList<>();
        today_temp = new ArrayList<>();
        week = new ArrayList<>();
        week_temp = new ArrayList<>();
        far = new ArrayList<>();
        far_temp = new ArrayList<>();
        // data structures for RecyclerView.

        // TODO: Sort items by dates.
        checklist_previous = (RecyclerView) rootView.findViewById(R.id.checklist_recycler_previous);
        checklist_today = (RecyclerView) rootView.findViewById(R.id.checklist_recycler_today);
        checklist_week = (RecyclerView) rootView.findViewById(R.id.checklist_recycler_week);
        checklist_far = (RecyclerView) rootView.findViewById(R.id.checklist_recycler_far);
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

    private RangeCategory dateComparator(String current, String target) {
        try {
            Date currentDate = new SimpleDateFormat("YYYY/MM/dd").parse(current);
            Date targetDate = new SimpleDateFormat("YYYY/MM/dd").parse(target);
            int differenceDate = target.compareTo(current);
            if (differenceDate > 7) {
                return RangeCategory.FAR;
            } else if (differenceDate > 0) {
                return RangeCategory.WEEK;
            } else if (differenceDate == 0) {
                return RangeCategory.TODAY;
            } else {
                return RangeCategory.BEFORE;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return RangeCategory.TODAY;
    }
}
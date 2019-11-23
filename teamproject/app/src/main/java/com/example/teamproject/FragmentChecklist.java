package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentChecklist extends Fragment {
    public static final int CHECKLIST_ADD_ITEM_REQUEST = 1000;
    private RecyclerView checklist_previous, checklist_today, checklist_tomorrow, checklist_week;
    private RecyclerView.Adapter previous_adapter, today_adapter, tomorrow_adapter, week_adapter;
    private RecyclerView.LayoutManager previous_manager, today_manager, tomorrow_manager, week_manager;
    private ArrayList<String> previous, today, tomorrow, week;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.checklist_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.checklist_menu_add:
                Intent intent = new Intent(getActivity(), CheckListItemAdd.class);
                startActivityForResult(intent, CHECKLIST_ADD_ITEM_REQUEST);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checklist, container, false);
        previous = new ArrayList<>();
        today = new ArrayList<>();
        tomorrow = new ArrayList<>();
        week = new ArrayList<>();
//      ----------test-------------
        previous.add("Do 1");
        previous.add("Do 2");
        previous.add("Do 3");
        previous.add("Do 4");
        previous.add("Do 6");
        previous.add("Do 7");
        previous.add("Do 8");
        previous.add("Do 9");

        today.add("Go 1");
        today.add("Go 2");
        today.add("Go 3");
        today.add("Go 4");
        today.add("Go 5");
        today.add("Go 6");
        today.add("Go 7");
        today.add("Go 8");
        today.add("Go 9");

        tomorrow.add("Buy 1");
        tomorrow.add("Buy 2");
        tomorrow.add("Buy 3");
        tomorrow.add("Buy 4");
        tomorrow.add("Buy 5");
        tomorrow.add("Buy 6");
        tomorrow.add("Buy 7");
        tomorrow.add("Buy 8");
        tomorrow.add("Buy 9");


        week.add("Interview 1");
        week.add("Interview 2");
        week.add("Interview 3");
        week.add("Interview 4");
        week.add("Interview 5");
        week.add("Interview 6");
        week.add("Interview 7");
        week.add("Interview 8");
        week.add("Interview 9");


//        -------------------------

        checklist_previous = (RecyclerView)rootView.findViewById(R.id.checklist_recycler_previous);
        checklist_previous.setHasFixedSize(true);
        checklist_today = (RecyclerView)rootView.findViewById(R.id.checklist_recycler_today);
        checklist_today.setHasFixedSize(true);

        checklist_tomorrow = (RecyclerView)rootView.findViewById(R.id.checklist_recycler_tomorrow);
        checklist_tomorrow.setHasFixedSize(true);
        checklist_week = (RecyclerView)rootView.findViewById(R.id.checklist_recycler_week);
        checklist_week.setHasFixedSize(true);

        previous_manager = new LinearLayoutManager(getActivity());
        today_manager = new LinearLayoutManager(getActivity());
        tomorrow_manager = new LinearLayoutManager(getActivity());
        week_manager = new LinearLayoutManager(getActivity());

        checklist_previous.setLayoutManager(previous_manager);
        checklist_today.setLayoutManager(today_manager);
        checklist_tomorrow.setLayoutManager(tomorrow_manager);
        checklist_week.setLayoutManager(week_manager);

        previous_adapter = new CheckListAdapter(previous);
        today_adapter = new CheckListAdapter(today);
        tomorrow_adapter = new CheckListAdapter(tomorrow);
        week_adapter = new CheckListAdapter(week);

        checklist_previous.setAdapter(previous_adapter);
        checklist_today.setAdapter(today_adapter);
        checklist_tomorrow.setAdapter(tomorrow_adapter);
        checklist_week.setAdapter(week_adapter);

        return rootView;
    }
}
package com.example.teamproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentChecklist extends Fragment {

//    private ListView m_ListView;            // ListView 를 위한 변수
//    private ArrayList<String> list;         // ListView 에 들어갈 값을 저장할 ArrayList
//    private ArrayAdapter<String> m_Adapter; // ArrayList 와 연결될 ArrayAdapter
//    String FILENAME = "LOL.txt";            // 입력한 값이 저장될 텍스트 파일 이름

    private RecyclerView checklist_previous, checklist_today, checklist_tomorrow, checklist_week;
    private RecyclerView.Adapter previous_adapter, today_adapter, tomorrow_adapter, week_adapter;
    private RecyclerView.LayoutManager previous_manager, today_manager, tomorrow_manager, week_manager;
    private ArrayList<String> previous, today, tomorrow, week;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);
        previous = new ArrayList<>();
        today = new ArrayList<>();
        tomorrow = new ArrayList<>();
        week = new ArrayList<>();
//      ----------test-------------
        previous.add("Do laundry");
        today.add("Go to school");
        today.add("Buy some foods");
        // tomorrow.add("Do nothing on tomorrow");
        week.add("Interview with Manager");
//        -------------------------

        checklist_previous = (RecyclerView)view.findViewById(R.id.checklist_recycler_previous);
        checklist_today = (RecyclerView)view.findViewById(R.id.checklist_recycler_today);
        checklist_tomorrow = (RecyclerView)view.findViewById(R.id.checklist_recycler_tomorrow);
        checklist_week = (RecyclerView)view.findViewById(R.id.checklist_recycler_week);

        previous_manager = new LinearLayoutManager(getContext());
        today_manager = new LinearLayoutManager(getContext());
        tomorrow_manager = new LinearLayoutManager(getContext());
        week_manager = new LinearLayoutManager(getContext());

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

//        m_ListView = (ListView) view.findViewById(R.id.checklist_recycler_previous);
//
//       m_Adapter = new ArrayAdapter<String>(
//               getActivity(),
//               android.R.layout.simple_list_item_1, list);
//        m_ListView.setAdapter(m_Adapter);
//        list.add("A");
//        list.add("B");
//        list.add("C");
//        list.add("D");
//        list.add("E");
//        list.add("F");

        return view;
    }
}
package com.example.teamproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FragmentChecklist extends Fragment {

    private ListView m_ListView;            // ListView 를 위한 변수
    private ArrayList<String> list;         // ListView 에 들어갈 값을 저장할 ArrayList
    private ArrayAdapter<String> m_Adapter; // ArrayList 와 연결될 ArrayAdapter
    String FILENAME = "LOL.txt";            // 입력한 값이 저장될 텍스트 파일 이름

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);

        list = new ArrayList<String>();

        m_ListView = (ListView) view.findViewById(R.id.today_list);

       m_Adapter = new ArrayAdapter<String>(
               getActivity(),
               android.R.layout.simple_list_item_1, list);
        m_ListView.setAdapter(m_Adapter);
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");


        return view;
    }
}
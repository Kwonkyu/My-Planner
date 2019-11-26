package com.example.teamproject.Timetable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.teamproject.Model.Lecture;
import com.example.teamproject.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class TimetableDialog extends DialogFragment {
    private Fragment fragment;
    String[] day = {"월", "화", "수", "목", "금", "토", "일"};
    int value;
    public TimetableDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_timetable, container, false);

        Bundle args = getArguments();
        value = args.getInt("key");

        fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        TextView name = view.findViewById(R.id.dialog_name);
        TextView room = view.findViewById(R.id.dialog_room);
        TextView lecturer = view.findViewById(R.id.dialog_lecturer);
        Button modifyBtn = view.findViewById(R.id.lecture_modify_button);
        Button deleteBtn = view.findViewById(R.id.lecture_delete_button);
        ListView list = view.findViewById(R.id.dialog_lecture_list);

        //리스트 뷰
        ArrayList<Lecture> lecList = new ArrayList<>();
        try {

            FileInputStream fin = getActivity().openFileInput("timetable.txt");
            BufferedInputStream bin = new BufferedInputStream(fin);
            ObjectInputStream oin = new ObjectInputStream(bin);
            lecList = (ArrayList<Lecture>) oin.readObject();
            oin.close();
        } catch (Exception e) {

        }
        Lecture lecture = lecList.get(value);
        name.setText(lecture.getName());
        room.setText(lecture.getRoom());
        lecturer.setText(lecture.getLecturer());

        //리스트 뷰 관련
        ArrayList<String> item = new ArrayList<>();
        for (Lecture.Time t : lecture.getLectureTime()) {
            item.add(t.getInfo("string"));

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, item);
        list.setAdapter(adapter);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((FragmentTimetable)getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout)).deleteLecture(value);
                    ((FragmentTimetable)getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout)).createTimeTable(9,18);
                } catch (Exception e) {
                    Log.d("tttt", e.getMessage());
                }
                dismiss();
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTimetableActivity.class);
                intent.putExtra("mode", "REVISE");
                intent.putExtra("id", value);
                startActivityForResult(intent, 2);
                dismiss();
            }
        });
        return view;
    }
}


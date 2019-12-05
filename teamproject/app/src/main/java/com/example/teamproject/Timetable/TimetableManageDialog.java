package com.example.teamproject.Timetable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.teamproject.MainActivity;
import com.example.teamproject.Model.Lecture;
import com.example.teamproject.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TimetableManageDialog extends DialogFragment {
    private Fragment fragment;
    String currentTimetable, mode;

    public TimetableManageDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_timetable_manage, container, false);


        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText timetableName = view.findViewById(R.id.timetable_name);
        Button okBtn = view.findViewById(R.id.timetable_ok_button);
        Button cancelBtn = view.findViewById(R.id.timetable_cancel_button);

        Bundle args = getArguments();
        mode = args.getString("mode");
        if(mode.equals("REVISE")) {
            currentTimetable = args.getString("filename");
            ((TextView)view.findViewById(R.id.timetable_text)).setText("시간표 수정");
            timetableName.setText(currentTimetable);
        }

        fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timetableName.getText().equals("")) {
                    Toast.makeText(getContext(), "이름을 입력하지 않았습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(((MainActivity)getActivity()).spinnerArray.contains(timetableName.getText().toString())) {
                    Toast.makeText(getContext(), "해당 이름을 가진 시간표가 이미 존재합니다.",Toast.LENGTH_SHORT).show();
                    return;
                }



                if(mode.equals("ADD")) {
                    currentTimetable = timetableName.getText().toString();
                    ((MainActivity)getActivity()).spinnerArray.add(currentTimetable);
                    ((MainActivity)getActivity()).adapter.notifyDataSetChanged();
                    ((MainActivity)getActivity()).spinner.setSelection(((MainActivity)getActivity()).spinnerArray.size() - 1);

                    //파일을 저장
                    try {
                        FileOutputStream fout = getActivity().openFileOutput("timetable_" + currentTimetable + ".txt", Context.MODE_PRIVATE);
                        fout.write("".getBytes());
                        fout.close();
                    } catch (Exception e) {

                    }
                }

                else {
                    File file = new File((getActivity()).getFilesDir().getAbsolutePath(),"timetable_" + currentTimetable + ".txt");
                    long creationTime = file.lastModified();
                    file.renameTo(new File((getActivity()).getFilesDir().getAbsolutePath(),"timetable_" + timetableName.getText().toString() + ".txt"));
                    file.setLastModified(creationTime);
                    ((MainActivity)getActivity()).spinnerArray.set(((MainActivity)getActivity()).spinnerArray.indexOf(currentTimetable), timetableName.getText().toString());
                    ((MainActivity)getActivity()).adapter.notifyDataSetChanged();
                    currentTimetable = timetableName.getText().toString();
                }

                try {
                    ((FragmentTimetable)getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout)).createTimeTable(9,18, currentTimetable);
                } catch (Exception e) {
                    Log.d("tttt", currentTimetable);
                }
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}

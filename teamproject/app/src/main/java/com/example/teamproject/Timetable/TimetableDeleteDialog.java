package com.example.teamproject.Timetable;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.teamproject.MainActivity;
import com.example.teamproject.R;

import java.io.File;
import java.io.FileOutputStream;

public class TimetableDeleteDialog extends DialogFragment {
    private Fragment fragment;
    String currentTimetable;

    public TimetableDeleteDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_timetable_detete, container, false);


        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        Button deleteBtn = view.findViewById(R.id.timetable_delete_button);
        Button cancelBtn = view.findViewById(R.id.timetable_cancel_button2);

        fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        Bundle args = getArguments();
        currentTimetable = args.getString("filename");

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainActivity) getActivity()).spinnerArray.size() == 1) {
                    Toast.makeText(getContext(), "최소 1개의 시간표는 필요합니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    File file = new File((getActivity()).getFilesDir().getAbsolutePath(), "timetable_" + currentTimetable + ".txt");
                    file.delete();
                    int index = ((MainActivity) getActivity()).spinnerArray.indexOf(currentTimetable);
                    if (index == ((MainActivity) getActivity()).spinnerArray.size() - 1) index--;
                    ((MainActivity) getActivity()).spinnerArray.remove(currentTimetable);
                    ((MainActivity) getActivity()).adapter.notifyDataSetChanged();
                    ((MainActivity) getActivity()).spinner.setSelection(index);
                    currentTimetable = ((MainActivity) getActivity()).spinnerArray.get(index);
                }

                try {
                    ((FragmentTimetable) getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout)).createTimeTable(9, 18, currentTimetable);
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
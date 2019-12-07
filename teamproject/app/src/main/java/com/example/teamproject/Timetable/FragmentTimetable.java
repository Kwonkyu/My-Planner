package com.example.teamproject.Timetable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.teamproject.Model.DAY;
import com.example.teamproject.Model.Lecture;
import com.example.teamproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class FragmentTimetable extends Fragment implements View.OnLongClickListener{
    TableLayout tl1, tl2;
    View v;
    ArrayList<Lecture> list;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_timetable, container, false);
        tl1 = v.findViewById(R.id.timetable_background);
        tl2 = v.findViewById(R.id.timetable);
        createTimeTableBackground(9, 18);
        try {
            createTimeTable(9, 18);
        } catch (Exception e) {
            Log.d("test11", e.getMessage());
        }

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTimetableActivity.class);
                intent.putExtra("mode", "ADD");
                startActivityForResult(intent, 1);
            }
        });
        return v;
    }

    private void createTimeTableBackground(int startT, int endT){
        TableRow tr;
        TextView tv;

        //레이아웃 초기화
        tl1.removeAllViewsInLayout();

        //정한 시간만큼 행 추가
        for(int i = startT - 1; i < endT; i++) {
            tr = new TableRow(getActivity());
            if(i == startT - 1) tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            else {
                tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), 1));
            }
            //각 열 추가
            for(int j = -1; j < DAY.values().length; j++){
                tv = new TextView(getActivity());

                //공통적인 속성 설정
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edge));

                //기본 텍스트 및 id 추가
                if(i == startT - 1 && j != -1) tv.setText(DAY.values()[j].getDay());
                else if (i != startT - 1 && j == -1) tv.setText(Integer.toString(i));

                //행 레이아웃 설정
                if(i == startT - 1) {
                    if(j == -1) tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    else tv.setLayoutParams(new TableRow.LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), TableRow.LayoutParams.WRAP_CONTENT, 1));
                }
                else {
                    if(j == -1) tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                    else tv.setLayoutParams(new TableRow.LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), TableRow.LayoutParams.MATCH_PARENT, 1));
                }
                tr.addView(tv);
            }
            tl1.addView(tr);
        }
    }

    public void createTimeTable(int startT, int endT) throws Exception{
        TableRow tr;
        TextView tv;

        //레이아웃 초기화
        tl2.removeAllViewsInLayout();

        //파일 읽기
        ArrayList<String> lectureList= new ArrayList<>();
        list = new ArrayList<>();

        //파일을 읽어옴
        FileInputStream fin = getActivity().openFileInput("timetable.txt");
        BufferedInputStream bin = new BufferedInputStream(fin);
        ObjectInputStream oin = new ObjectInputStream(bin);
        list = (ArrayList<Lecture>) oin.readObject();
        oin.close();

        for(Lecture l : list) {
            lectureList.addAll(l.getLectureInfo());
        }
        Collections.sort(lectureList);

        for(String l : lectureList) {
            Log.d("test11", l);
        }
        //정한 시간만큼 행 추가
        for(int i = 0; i < 2; i++) {
            tr = new TableRow(getActivity());
            if(i == 0) tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            else {
                tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), 1));
            }
            //각 열 추가
            for(int j = -1; j < DAY.values().length; j++){

                if(i == 0 || j == -1) {

                    tv = new TextView(getActivity());
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(14);
                    tv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edge));
                    tv.setVisibility(View.INVISIBLE);
                    if(i == 0 && j == -1) {
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    }
                    else if(i == 0){
                        tv.setLayoutParams(new TableRow.LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), TableRow.LayoutParams.WRAP_CONTENT, 1));
                        tv.setText(DAY.values()[j].getDay());
                    }
                    else {
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                        tv.setText("10");
                    }
                    tr.addView(tv);
                }

                else{
                    LinearLayout ll = new LinearLayout(getActivity());
                    ll.setLayoutParams(new TableRow.LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), TableRow.LayoutParams.MATCH_PARENT, 1));
                    ll.setOrientation(LinearLayout.VERTICAL);
                    tr.addView(ll);

                    Calendar time = Calendar.getInstance();
                    setTime(time, startT < 10 ? ("0" + startT + ":00") : (startT + ":00"));
                    int weight = startT * 60;
                    for(int k = 0; k < lectureList.size(); k++) {
                        //임시 Lecture 객체 이용
                        final Lecture currentItem = new Lecture();
                        String[] attribute = lectureList.get(k).split(",");
                        currentItem.setId(Integer.parseInt(attribute[1]));
                        currentItem.setColor(attribute[2]);
                        currentItem.setName(attribute[3]);
                        currentItem.setRoom(attribute[4]);
                        currentItem.setLecturer(attribute[5]);

                        if(weight == 0) break;

                        //시간표 내용 작성
                        if(lectureList.get(k).substring(0,1).equals(Integer.toString(j))){
                            Calendar startTime = Calendar.getInstance();
                            setTime(startTime, attribute[0].substring(2,7));;
                            Calendar endTime = Calendar.getInstance();
                            setTime(endTime, attribute[0].substring(8));
                            long min;

                            //빈 공간을 차지하는 텍스트 뷰 생성
                            if(time.getTime() != startTime.getTime()){
                                tv = new TextView(getActivity());
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(14);
                                min = (startTime.getTimeInMillis() - time.getTimeInMillis()) / 60000;
                                weight -= min;
                                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()),  min));
                                ll.addView(tv);
                            }

                            tv = new TextView(getActivity());
                            tv.setText(currentItem.getName() + "\n" + currentItem.getRoom() + "\n" + currentItem.getLecturer());
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(14);
                            tv.setTextColor(Color.rgb(255,255,255));
                            tv.setTag(currentItem.getId());
                            tv.setOnLongClickListener(this);
                            min = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 60000;
                            time = endTime;
                            weight -= min;
                            if(min != 0) {
                                tv.setBackgroundColor(Color.parseColor(currentItem.getColor()));
                            }
                            else {

                            }
                            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), min));
                            ll.addView(tv);
                            if(k == lectureList.size() - 1 && weight != 0) {
                                tv = new TextView(getActivity());
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(14);
                                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()),  weight));
                                ll.addView(tv);
                            }
                        }
                        else {
                            if(Integer.parseInt(lectureList.get(k).substring(0,1)) > j + 1 && weight != 0) {
                                tv = new TextView(getActivity());
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(14);
                                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()),  weight));
                                weight = 0;
                                ll.addView(tv);
                            }
                        }
                    }
                }
            }
            tl2.addView(tr);
        }
    }

    public void deleteLecture(int id) {
        try{
            list.remove(id);
            for(Lecture l : list) {
                l.setId(list.indexOf(l));
            }
            FileOutputStream fout = getActivity().openFileOutput("timetable.txt", Context.MODE_PRIVATE);
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            ObjectOutputStream oout = new ObjectOutputStream(bout);

            oout.writeObject(list);
            oout.close();
        }catch (Exception e){
            Log.d("tttt", e.getMessage());
        }
    }

    public void setTime(Calendar calendar, String time) {
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3,5)));
        calendar.set(Calendar.SECOND, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK) {
            if (requestCode == 1) {
                try {
                    createTimeTable(9, 18);
                } catch (Exception e) {
                }
            }
            else if(requestCode == 2) {
                try {
                    createTimeTable(9, 18);
                } catch (Exception e) {
                }
            }
        }
        else {

        }
    }

    @Override
    public boolean onLongClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("key", (int)v.getTag());
        TimetableDialog dialog = new TimetableDialog();
        dialog.setArguments(bundle);
        dialog.show(getActivity().getSupportFragmentManager(), "1");


        return false;
    }
}
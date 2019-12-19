package com.example.teamproject.Timetable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.teamproject.MainActivity;
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
    String currentTimetable;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_timetable, container, false);
        setHasOptionsMenu(true);

        tl1 = v.findViewById(R.id.timetable_background);
        tl2 = v.findViewById(R.id.timetable);


        currentTimetable = ((MainActivity)getActivity()).spinner.getSelectedItem().toString();
        ((MainActivity)getActivity()).spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentTimetable = ((MainActivity)getActivity()).adapter.getItem(position);
                try {
                    createTimeTable(9, 18, currentTimetable);
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        createTimeTableBackground(9, 18);
        try {
            createTimeTable(9, 18, currentTimetable);
        } catch (Exception e) {
        }

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTimetableActivity.class);
                intent.putExtra("mode", "ADD");
                intent.putExtra("filename", currentTimetable);
                startActivityForResult(intent, 1);
            }
        });
        return v;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.timetable_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.timetable_add :
                Bundle bundle = new Bundle();
                bundle.putString("mode","ADD");
                TimetableManageDialog dialog = new TimetableManageDialog();
                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), "1");
                break;
            case R.id.timetable_revise:
                bundle = new Bundle();
                bundle.putString("mode","REVISE");
                bundle.putString("filename", currentTimetable);
                dialog = new TimetableManageDialog();
                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), "2");
                break;
            case R.id.timetable_delete:
                bundle = new Bundle();
                bundle.putString("filename", currentTimetable);
                TimetableDeleteDialog deleteDialog = new TimetableDeleteDialog();
                deleteDialog.setArguments(bundle);
                deleteDialog.show(getActivity().getSupportFragmentManager(), "1");
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
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
                GradientDrawable drawable = (GradientDrawable)ContextCompat.getDrawable(getActivity(), R.drawable.edge);
                drawable.setColor(Color.parseColor("#FFFFFF"));
                tv.setBackground(drawable);

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

    public void createTimeTable(int startT, int endT, String filename) throws Exception{
        TableRow tr;
        TextView tv;

        //레이아웃 초기화
        tl2.removeAllViewsInLayout();

        //파일 읽기
        ArrayList<String> lectureList= new ArrayList<>();
        list = new ArrayList<>();

        //파일을 읽어옴
        FileInputStream fin = getActivity().openFileInput("timetable_" + filename + ".txt");
        BufferedInputStream bin = new BufferedInputStream(fin);
        ObjectInputStream oin = new ObjectInputStream(bin);
        list = (ArrayList<Lecture>) oin.readObject();
        oin.close();

        for(Lecture l : list) {
            lectureList.addAll(l.getLectureInfo());
        }
        Collections.sort(lectureList);

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
                                Log.d(j + " test : 사전 빈공간", Long.toString(min));
                                ll.addView(tv);
                            }

                            //시간표 생성
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
                                GradientDrawable drawable = (GradientDrawable)ContextCompat.getDrawable(getActivity(), R.drawable.edge);
                                drawable.setColor(Color.parseColor(currentItem.getColor()));
                                tv.setBackground(drawable);
                            }

                            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), min));
                            Log.d(j + " test : 핵심", Long.toString(min));
                            ll.addView(tv);

                            if(k == lectureList.size() - 1 && weight != 0) {
                                tv = new TextView(getActivity());
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(14);
                                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()),  weight));
                                Log.d(j + " test : 사후 빈공간", Long.toString(weight));
                                ll.addView(tv);
                            }
                        }
                        else {
                            if(Integer.parseInt(lectureList.get(k).substring(0,1)) >= j + 1 && weight != 0) {
                                tv = new TextView(getActivity());
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(14);
                                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()),  weight));
                                weight = 0;
                                Log.d(j + " test : 사후 빈공간", Long.toString(weight));
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
            FileOutputStream fout = getActivity().openFileOutput("timetable_" + currentTimetable + ".txt", Context.MODE_PRIVATE);
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            ObjectOutputStream oout = new ObjectOutputStream(bout);

            oout.writeObject(list);
            oout.close();
        }catch (Exception e){
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
                    createTimeTable(9, 18, currentTimetable);
                } catch (Exception e) {
                }
            }
            else if(requestCode == 2) {
                try {
                    createTimeTable(9, 18, currentTimetable);
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
        bundle.putString("filename", currentTimetable);
        TimetableDialog dialog = new TimetableDialog();
        dialog.setArguments(bundle);
        dialog.show(getActivity().getSupportFragmentManager(), "1");
        return false;
    }
}
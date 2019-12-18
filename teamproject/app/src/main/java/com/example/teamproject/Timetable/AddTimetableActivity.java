package com.example.teamproject.Timetable;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamproject.Model.DAY;
import com.example.teamproject.Model.Lecture;
import com.example.teamproject.R;
import com.example.teamproject.Views.ColorAdapter;
import com.example.teamproject.Views.ColorItemDecoration;
import com.example.teamproject.Views.ListViewBtnAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class AddTimetableActivity extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener {
    TextView tv1, tv2, tv3;
    RecyclerView recycle;
    ColorAdapter adapter2;
    CheckBox[] checkBoxes;
    ArrayList<String> colorItems;
    ArrayList<Lecture> lecList;
    String selectColor, mode, currentTimetable;
    ListView list;
    ListViewBtnAdapter adapter;
    ArrayList<String> items;
    TextView startTimeTextView, endTimeTextView;
    String[] colors = {"#f78c6c","#feb44d", "#fbea55", "#e2e650", "#9ff4b9", "#42dc28", "#7acee1", "#d2a2e4"};
    Long creationTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);

        mode = getIntent().getStringExtra("mode");
        currentTimetable = getIntent().getStringExtra("filename");
        init();

        //파일을 미리 불러옴
        lecList = new ArrayList<>();
        try {
            File file = new File(getFilesDir().getAbsolutePath(), "timetable_" + currentTimetable + ".txt");
            creationTime = file.lastModified();

            //파일을 읽어옴
            FileInputStream in = openFileInput("timetable_" + currentTimetable + ".txt");
            BufferedInputStream bin = new BufferedInputStream(in);
            ObjectInputStream oin = new ObjectInputStream(bin);
            lecList = (ArrayList<Lecture>) oin.readObject();
            Log.d("ttttt",currentTimetable);
            oin.close();
        } catch(Exception e) {
            creationTime = (long)0;
        }

        // 수정 모드
        if(mode.equals("REVISE")) {
            Lecture lec = lecList.get(getIntent().getIntExtra("id",0));
            tv1.setText(lec.getName());
            tv2.setText(lec.getRoom());
            tv3.setText(lec.getLecturer());
            selectColor = lec.getColor();
            for(Lecture.Time t : lec.getLectureTime()) {
                items.add(t.getInfo("string"));
            }
            adapter.notifyDataSetChanged();
        }
    }

    //기본적인 설정
    public void init() {
        //툴바 설정
        Toolbar tb = (Toolbar) findViewById(R.id.add_timetable_toolbar) ;
        setSupportActionBar(tb) ;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(mode.equals("REVISE")) {
            TextView toolbarTitle = findViewById(R.id.toolbar_title);
            toolbarTitle.setText("시간표 수정");
        }
        //텍스트 뷰 설정
        tv1 = findViewById(R.id.lecture_name);
        tv2 = findViewById(R.id.lecture_room);
        tv3 = findViewById(R.id.lecturer);

        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if(ps.matcher(source).matches()){
                    return source;
                }
                return "";
            }
        };
        InputFilter[] filters = new InputFilter[] {inputFilter};

        tv1.setFilters(filters);
        tv2.setFilters(filters);
        tv3.setFilters(filters);

        //컬러 리사이클러 뷰 설정
        recycle = findViewById(R.id.color_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycle.setLayoutManager(layoutManager);
        recycle.addItemDecoration(new ColorItemDecoration(50));
        colorItems = new ArrayList<>();
        for(String s : colors) {
            colorItems.add(s);
        }
        selectColor = "#f78c6c";
        //임시, 바꿔야함
        adapter2 = new ColorAdapter(this, colorItems);

        recycle.setAdapter(adapter2);

        //체크 박스 설정
        checkBoxes = new CheckBox[7];
        checkBoxes[0] = findViewById(R.id.check_mon);
        checkBoxes[1] = findViewById(R.id.check_tue);
        checkBoxes[2] = findViewById(R.id.check_wed);
        checkBoxes[3] = findViewById(R.id.check_thu);
        checkBoxes[4] = findViewById(R.id.check_fri);
        checkBoxes[5] = findViewById(R.id.check_sat);
        checkBoxes[6] = findViewById(R.id.check_sun);



        for(int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) buttonView.setTextColor(Color.rgb(255,255,255));
                    else buttonView.setTextColor(Color.rgb(0,0,0));
                }
            });
        }
        //리스트 뷰 설정
        list = findViewById(R.id.time_list);
        items = new ArrayList<>();
        adapter = new ListViewBtnAdapter(this, R.layout.listview_btn_item, items, this) ;
        list = findViewById(R.id.time_list);
        list.setAdapter(adapter);

        //시간 설정
        startTimeTextView = findViewById(R.id.beginTime);
        endTimeTextView = findViewById(R.id.endTime);

        startTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTime();
            }
        });
        endTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTime();
            }
        });

        //버튼 설정
        Button btn = findViewById(R.id.list_add_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format =  new SimpleDateFormat("HH:mm");
                Calendar startT, endT, addStartT, addEndT;
                startT = Calendar.getInstance();
                endT = Calendar.getInstance();
                addStartT = Calendar.getInstance();
                addEndT = Calendar.getInstance();

                setTime(addStartT, startTimeTextView.getText().toString());
                setTime(addEndT, endTimeTextView.getText().toString());

                if(addStartT.getTimeInMillis() >= addEndT.getTimeInMillis()) {
                    Toast.makeText(getApplicationContext(),"시간을 잘못 입력하셨습니다.",Toast.LENGTH_LONG).show();
                    return;
                }

                ArrayList<String> tempItems = new ArrayList<>();
                for(DAY day : DAY.values()) {
                    if(checkBoxes[day.ordinal()].isChecked()) tempItems.add(day.getDay() + " " +startTimeTextView.getText().toString() + "~" +endTimeTextView.getText().toString());
                }

                for(Lecture l : lecList) {
                    for(Lecture.Time t : l.getLectureTime()) {
                        for(String s : tempItems) {
                            if(t.getDay().getDay().equals(s.substring(0,1))) {
                                startT = t.getStartTime();
                                endT = t.getEndTime();
                                setTime(addStartT, s.substring(2,7));
                                setTime(addEndT,s.substring(8));
                                if((addStartT.getTimeInMillis() < startT.getTimeInMillis() && addEndT.getTimeInMillis() > startT.getTimeInMillis()) ||
                                        (addStartT.getTimeInMillis() > startT.getTimeInMillis() && addStartT.getTimeInMillis() < endT.getTimeInMillis()) ||
                                        (addEndT.getTimeInMillis() > startT.getTimeInMillis() && addEndT.getTimeInMillis() < endT.getTimeInMillis())) {
                                    Toast.makeText(getApplicationContext(),"중복된 시간을 입력하셨습니다.",Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else if(addStartT.getTimeInMillis() == startT.getTimeInMillis() && addEndT.getTimeInMillis() == addEndT.getTimeInMillis()) {
                                    Toast.makeText(getApplicationContext(),"중복된 시간을 입력하셨습니다.",Toast.LENGTH_LONG).show();
                                    return;
                                }

                            }
                        }
                    }
                }

                for(String t : items) {
                    for(String s : tempItems) {
                        if(t.substring(0,1).equals(s.substring(0,1))) {
                            setTime(startT, t.substring(2,7));
                            setTime(endT,t.substring(8));
                            setTime(addStartT, s.substring(2,7));
                            setTime(addEndT,s.substring(8));
                            Log.d("testt",startT.getTime().toString());
                            Log.d("tests",addStartT.getTime().toString());
                            if((addStartT.getTimeInMillis() < startT.getTimeInMillis() && addEndT.getTimeInMillis() > startT.getTimeInMillis()) ||
                                    (addStartT.getTimeInMillis() > startT.getTimeInMillis() && addStartT.getTimeInMillis() < endT.getTimeInMillis()) ||
                                    (addEndT.getTimeInMillis() > startT.getTimeInMillis() && addEndT.getTimeInMillis() < endT.getTimeInMillis())) {
                                Toast.makeText(getApplicationContext(),"중복된 시간을 입력하셨습니다.",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else if(addStartT.getTimeInMillis() == startT.getTimeInMillis() && addEndT.getTimeInMillis() == addEndT.getTimeInMillis()) {
                                Toast.makeText(getApplicationContext(),"중복된 시간을 입력하셨습니다.",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                }

                items.addAll(tempItems);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_timetable_menu, menu) ;
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.add_timetable_button:
                if(tv1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"과목명은 필수입니다.",Toast.LENGTH_SHORT).show();
                    return true;
                }

                else if(items.size() == 0) {
                    Toast.makeText(getApplicationContext(),"1개 이상의 시간을 등록하십시오.",Toast.LENGTH_SHORT).show();
                    return true;
                }
                //추가 or 수정할 정보를 가지는 lecture 객체 생성
                Lecture lec = new Lecture();
                selectColor = colors[adapter2.selectedItem];
                lec.setColor(selectColor);
                lec.setId(mode.equals("ADD") ? lecList.size() : getIntent().getIntExtra("id",0));
                lec.setName(tv1.getText().toString());
                lec.setRoom(tv2.getText().toString());
                lec.setLecturer(tv3.getText().toString());
                for(String str : items) {
                    for(DAY day : DAY.values()) {
                        if(day.getDay().equals(str.substring(0,1))) {
                            lec.addTime(day, str.substring(2, 7), str.substring(8));
                            break;
                        }
                    }
                }

                //모드에 따라서 행동 변화
                if(mode.equals("ADD")) lecList.add(lec);
                else if(mode.equals("REVISE")) lecList.set(lec.getId(), lec);

                for(Lecture l : lecList) {
                    for(String s : l.getLectureInfo()) Log.d("ttt", s);
                }
                //파일을 저장
                try{
                    FileOutputStream fout = openFileOutput("timetable_" + currentTimetable + ".txt", Context.MODE_PRIVATE);
                    BufferedOutputStream bout = new BufferedOutputStream(fout);
                    ObjectOutputStream oout = new ObjectOutputStream(bout);
                    oout.writeObject(lecList);
                    oout.close();

                    File file = new File(getFilesDir().getAbsolutePath(), "timetable_" + currentTimetable + ".txt");
                    if(creationTime == (long)0) {
                        file.setLastModified(System.currentTimeMillis());
                    }
                    else {
                        file.setLastModified(creationTime);
                    }

                    setResult(RESULT_OK);
                } catch(Exception e) {
                    setResult(RESULT_CANCELED);
                }
                finish();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListBtnClick(int position) {
        items.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void showStartTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay < 9) {
                    hourOfDay = 9;
                    minute = 0;
                    Toast.makeText(getApplicationContext(),"선택할 수 있는 가장 빠른 시간은 오전 9시입니다.",Toast.LENGTH_SHORT).show();
                }
                else if(hourOfDay > 18 || (hourOfDay == 18 && minute > 0)) {
                    hourOfDay = 18;
                    minute = 0;
                    Toast.makeText(getApplicationContext(),"선택할 수 있는 가장 늦은 시간은 오후 6시입니다.",Toast.LENGTH_SHORT).show();
                }
                startTimeTextView.setText((hourOfDay < 10 ?  "0" + hourOfDay : hourOfDay) + ":"  + (minute < 10 ? "0" + minute : minute));
            }
        }, 9, 0, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        timePickerDialog.show();
    }

    public void showEndTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay < 9) {
                    hourOfDay = 9;
                    minute = 0;
                    Toast.makeText(getApplicationContext(),"선택할 수 있는 가장 빠른 시간은 오전 9시입니다.",Toast.LENGTH_SHORT).show();
                }
                else if(hourOfDay > 18 || (hourOfDay == 18 && minute > 0)) {
                    hourOfDay = 18;
                    minute = 0;
                    Toast.makeText(getApplicationContext(),"선택할 수 있는 가장 늦은 시간은 오후 6시입니다.",Toast.LENGTH_SHORT).show();
                }
                endTimeTextView.setText((hourOfDay < 10 ?  "0" + hourOfDay : hourOfDay) + ":"  + (minute < 10 ? "0" + minute : minute));
            }
        }, 18, 0, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    public void setTime(Calendar calendar, String time) {
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3,5)));
        calendar.set(Calendar.SECOND, 0);
    }
}

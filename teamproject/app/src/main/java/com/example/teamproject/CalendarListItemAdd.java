package com.example.teamproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CalendarListItemAdd extends AppCompatActivity {

    String todo;

    EditText calendarlist_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.calendarlist_item_add);
        super.onCreate(savedInstanceState);

        calendarlist_add = findViewById(R.id.calendarlist_edit_add);
        Button addBtn = findViewById(R.id.add_button);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 버튼이 눌렸을 때 작동하는 함수

                    todo = calendarlist_add.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("ADD_TEXT", todo);
                    setResult(RESULT_OK, intent);
                    finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendarlist_additem_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.calendarlist_menu_save:
                Toast.makeText(getApplicationContext(), "Still in development", Toast.LENGTH_SHORT).show();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }



}

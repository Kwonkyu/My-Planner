package com.example.teamproject;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class CheckListItemAdd extends AppCompatActivity {
    private EditText input;
    private TextView dateInput;

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        boolean init = false;
        TextView dateText;
        void setTextView(TextView date) {
            dateText = date;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            if(!init) {

                init = true;
            }
            super.onCreate(savedInstanceState);
        }

        @Override @NonNull @TargetApi(26)
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO: How to init DatePicker's default as today, and keep user's selection after dialog is closed?
            // TODO: Need a clear, simple and effective way to translate dates between textview and intent's data.
            String[] ymd = dateText.getText().toString().split("/");
            int[] ymdInt = new int[3];
            for(int i=0;i<3;i++) ymdInt[i] = Integer.parseInt(ymd[i]);
            return new DatePickerDialog(getActivity(), this, ymdInt[0], ymdInt[1]-1, ymdInt[2]);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateText.setText(String.format(Locale.KOREAN, "%d/%d/%d", year, monthOfYear+1, dayOfMonth));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.checklist_item_add);

        input = findViewById(R.id.checklist_edit_add);
        dateInput = findViewById(R.id.checklist_text_date);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String currentDate = String.format(Locale.KOREAN, "%d/%d/%d", year, month + 1, day);
        dateInput.setText(currentDate);

        final DatePickerFragment dpf = new DatePickerFragment();
        dpf.setTextView(dateInput);
        // Note: Don't make instance of DatePickerFragment in setOnClickListener. It keeps re-date dateInput.

        Button callDatePicker = findViewById(R.id.checklist_button_datepicker);
        callDatePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dpf.show(getSupportFragmentManager(), "datePicker");
            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checklist_additem_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.checklist_menu_save:
                String text = input.getText().toString();
                Intent intent = new Intent(this, FragmentChecklist.class);
                intent.putExtra(FragmentChecklist.CHECKLIST_ADD_ITEM_OK, text);
                intent.putExtra(FragmentChecklist.CHECKLIST_ADD_ITEM_DATE, dateInput.getText().toString());
                setResult(FragmentChecklist.CHECKLIST_ADD_ITEM_RESULT, intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

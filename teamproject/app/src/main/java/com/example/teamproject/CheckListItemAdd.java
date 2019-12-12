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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class CheckListItemAdd extends AppCompatActivity {
    private EditText input;
    private EditText place;
    private static String dateString;

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        Button dateButton;
        void setButtonView(Button button) { dateButton = button; }
        // link date string from constructor.

        @Override @NonNull @TargetApi(26)
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] ymd = dateString.split("/");
            // Split yyyy/mm/dd format date string with "/" delimiter.
            int[] ymdInt = new int[3];
            for(int i=0;i<3;i++) ymdInt[i] = Integer.parseInt(ymd[i]);
            // Parse date string element(year, month, day) to Integer.
            return new DatePickerDialog(getActivity(), this, ymdInt[0], ymdInt[1]-1, ymdInt[2]);
            // month should be decreased by 1 because of 0-indexing. See onDateSet().
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Set date string with increased month(by 1).
            dateButton.setText(String.format(Locale.KOREAN, "%d년 %d월 %d일", year, monthOfYear+1, dayOfMonth));
            dateString = String.format(Locale.KOREAN, "%d/%d/%d", year, monthOfYear+1, dayOfMonth);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.checklist_item_add);

        input = findViewById(R.id.checklist_edit_add);
        place = findViewById(R.id.checklist_edit_add_place);
        Button dateButton = findViewById(R.id.checklist_button_datepicker);
        // Link view to variable

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        dateButton.setText(String.format(Locale.KOREAN, "%d년 %d월 %d일", year, month + 1, day));
        dateString = String.format(Locale.KOREAN, "%d/%d/%d", year, month + 1, day);
        // Get today's date as format of yyyy/mm/dd string and set.

        final DatePickerFragment dpf = new DatePickerFragment();
        dpf.setButtonView(dateButton);
        // Note: Don't make instance of DatePickerFragment in setOnClickListener. It keeps re-date dateInput.

        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dpf.show(getSupportFragmentManager(), "datePicker");
                // Show DatePickerDialogFragment
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
                String placeText = place.getText().toString();
                Intent intent = new Intent(this, FragmentChecklist.class);
                intent.putExtra(FragmentChecklist.CHECKLIST_ADD_ITEM_OK, text);
                intent.putExtra(FragmentChecklist.CHECKLIST_ADD_ITEM_DATE, dateString);
                intent.putExtra(FragmentChecklist.CHECKLIST_ADD_ITEM_PLACE, placeText);
                setResult(FragmentChecklist.CHECKLIST_ADD_ITEM_RESULT, intent);
                // Get checklist item's text and expiration date as string and send as intent's data.
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

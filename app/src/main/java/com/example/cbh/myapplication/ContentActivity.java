package com.example.cbh.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class ContentActivity extends AppCompatActivity {
    private static EditText editText1;
    private static EditText editText2;
    private static int mYear, mMonth, mDay;
    private MyDatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.content);
        getSupportActionBar().hide();
        editText1 = (EditText) findViewById(R.id.set_time);
        editText2 = (EditText) findViewById(R.id.set_content);
        showNow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ContentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    public static void showNow() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }

    public static void updateDisplay() {
        editText1.setText(new StringBuilder()
                .append(mYear)
                .append("-")
                .append(pad(mMonth + 1))
                .append("-")
                .append(pad(mDay)));
    }

    public static String pad(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        } else {
            return "0" + String.valueOf(number);
        }
    }

    public static class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay();
        }
    }

    public void click_edit(View view) {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        datePickerDialogFragment.show(fragmentManager, "datePicker");
    }


    public void click_ok(View view) {
        dbHelper = new MyDatabaseHelper(this, "List.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", editText1.getText().toString());
        values.put("content", editText2.getText().toString());
        db.insert("List", null, values);
        values.clear();
        Intent intent = new Intent(ContentActivity.this, ShowContentActivity.class);
        intent.putExtra("time", editText1.getText().toString());
        intent.putExtra("content", editText2.getText().toString());
        startActivity(intent);
        finish();
    }
}

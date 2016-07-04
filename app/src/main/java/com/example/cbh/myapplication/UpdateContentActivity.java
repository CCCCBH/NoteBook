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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateContentActivity extends AppCompatActivity {
    private static EditText editText1;
    private static EditText editText2;
    private static int mYear, mMonth, mDay;
    private MyDatabaseHelper dbHelper;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_content);
        getSupportActionBar().hide();
        editText1 = (EditText) findViewById(R.id.update_time);
        editText2 = (EditText) findViewById(R.id.update_content);
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String content = intent.getStringExtra("content");
        id = intent.getIntExtra("id", 0);
        editText1.setText(time);
        editText2.setText(content);
        showNow();
    }

    public static void showNow() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

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

    public void click_update(View view) {
        dbHelper = new MyDatabaseHelper(this, "List.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", editText1.getText().toString());
        values.put("content", editText2.getText().toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date currentDate = new Date(System.currentTimeMillis());
        String last_time = simpleDateFormat.format(currentDate);
        values.put("last_time", last_time);
        String[] id_use = {String.valueOf(id)};
        db.update("List", values, "id=?", id_use);
        Intent intent = new Intent(UpdateContentActivity.this, MainActivity.class);
        startActivity(intent);
        MainActivity.instance.finish();
        ShowContentActivity.instance.finish();
        finish();
    }
}

package com.example.cbh.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowContentActivity extends AppCompatActivity {
    public static Activity instance = null;
    private TextView textView_time;
    private TextView textView_content;
    private MyDatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.show_content);
        getSupportActionBar().hide();
        instance = this;
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String content = intent.getStringExtra("content");
        textView_time = (TextView) findViewById(R.id.show_time);
        textView_content = (TextView) findViewById(R.id.show_content);
        textView_time.setText(time);
        textView_content.setText(content);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ShowContentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    public void click_edit_bt(View view) {
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String content = intent.getStringExtra("content");
        int id = intent.getIntExtra("id", 0);
        intent = new Intent(ShowContentActivity.this, UpdateContentActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("time", time);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    public void click_delete_bt(View view) {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String[] id_use = {String.valueOf(id)};
        dbHelper = new MyDatabaseHelper(this, "List.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("List", "id=?", id_use);
        Toast.makeText(ShowContentActivity.this, "Delete succeeded", Toast.LENGTH_LONG).show();
        intent = new Intent(ShowContentActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

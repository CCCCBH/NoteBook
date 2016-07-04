package com.example.cbh.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    public static Activity instance = null;
    public static final String TAG = "bat";
    private Button button;
    private ListView listView;
    private MyDatabaseHelper dbHelper;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        instance = this;
        dbHelper = new MyDatabaseHelper(this, "List.db", null, 1);
        dbHelper.getWritableDatabase();
        button = (Button) findViewById(R.id.new_item);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ShowContentActivity.class);
                intent.putExtra("id", (Integer) list.get(position).get("id"));
                intent.putExtra("time", (String) list.get(position).get("time"));
                intent.putExtra("content", (String) list.get(position).get("content"));
                startActivity(intent);
            }
        });
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("List", null, null, null, null, null, "last_time desc", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("time", time);
            map.put("content", content);
            list.add(map);
        }
        cursor.close();
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.listview, new String[]{"time", "content"}, new int[]{R.id.item_time, R.id.item_content});
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}

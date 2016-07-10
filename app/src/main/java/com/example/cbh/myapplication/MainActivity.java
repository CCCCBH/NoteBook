package com.example.cbh.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    public static Activity instance = null;
    private Button button;
    private ListView listView;
    private MyDatabaseHelper dbHelper;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = new HashMap<String, Object>();
    private int id;
    private String time;
    private String content;
    private int id_menu;
    private String time_menu;
    private String content_menu;

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
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.list);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ShowContentActivity.class);
                intent.putExtra("id", (Integer) list.get(position).get("id"));
                intent.putExtra("time", (String) list.get(position).get("time"));
                intent.putExtra("content", (String) list.get(position).get("content"));
                startActivity(intent);
                finish();
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                id_menu = (int) list.get(position).get("id");
                time_menu = (String) list.get(position).get("time");
                content_menu = (String) list.get(position).get("content");
                return false;
            }
        });

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("List", null, null, null, null, null, "last_time desc", null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            time = cursor.getString(cursor.getColumnIndex("time"));
            content = cursor.getString(cursor.getColumnIndex("content"));
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(MainActivity.this, UpdateContentActivity.class);
                intent.putExtra("id", id_menu);
                intent.putExtra("time", time_menu);
                intent.putExtra("content", content_menu);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_delete:
                String[] id_use = {String.valueOf(id_menu)};
                dbHelper = new MyDatabaseHelper(this, "List.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("List", "id=?", id_use);
                Toast.makeText(MainActivity.this, "Delete succeeded", Toast.LENGTH_LONG).show();
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

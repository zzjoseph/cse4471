package com.example.denny.qrcode;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by denny on 4/9/17.
 */

public class ListPage extends AppCompatActivity{
    private ListView lvItem;
    private ItemListAdapter adapter;
    private List<Item> itemList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        lvItem = (ListView)findViewById(R.id.listview_item);
        itemList = new ArrayList<>();

        //get data from database
        MyDBHandler dbHelper = new MyDBHandler(this, null ,null, 0);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.COLUMN_WEB, dbHelper.COLUMN_MAL};
        Cursor cursor = db.query(dbHelper.TABLE_CACHE, columns, null, null, null, null, null);
        while(cursor.moveToNext()) {
            String url = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_WEB));
            String safety = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_MAL));
            itemList.add(new Item(url, safety));
        }


        adapter = new ItemListAdapter(itemList, getApplicationContext());
        lvItem.setAdapter(adapter);

    }
}

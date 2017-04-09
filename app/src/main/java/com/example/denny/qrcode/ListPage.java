package com.example.denny.qrcode;

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
        itemList.add(new Item("www.google.com"));

        adapter = new ItemListAdapter(itemList, getApplicationContext());
        lvItem.setAdapter(adapter);

    }
}

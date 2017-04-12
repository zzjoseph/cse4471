package com.example.denny.qrcode;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by denny on 4/9/17.
 */

public class ItemListAdapter extends BaseAdapter {
    private Context context;
    private List<Item> itemList;

    public ItemListAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.list_item,null);
        TextView tvName = (TextView)v.findViewById(R.id.item_name);

        tvName.setText(itemList.get(position).getUrl());

        v.setTag(itemList.get(position).getUrl());

        return v;
    }
}

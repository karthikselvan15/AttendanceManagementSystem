package com.example.karthi.attendance;

/**
 * Created by Karthi on 17-03-2018.
 */

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    private final String[] IdList;
    private final String[] NameList;
    Context context;

    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] IdList, String[] NameList) {
        this.context = context;
        this.IdList = IdList;
        this.NameList = NameList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return IdList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        textView1.setText(IdList[i]);
        textView2.setText(NameList[i]);
        return view;

    }
}
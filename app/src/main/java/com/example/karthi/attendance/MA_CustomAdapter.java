package com.example.karthi.attendance;

/**
 * Created by Karthi on 17-03-2018.
 */

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MA_CustomAdapter extends BaseAdapter {
    private final String[] EmpId,EmpStatus;
    Context context;




    String[] list={"Present","Half-Day","Absent"};
    public static String[] SpinnerPosition;
    LayoutInflater inflter;


    public MA_CustomAdapter(Context applicationContext, String[] EmpId, String[] EmpStatus) {
        this.context = context;
        this.EmpId = EmpId;

        SpinnerPosition=new String[EmpId.length];

        this.EmpStatus = EmpStatus;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return EmpId.length;
    }

    @Override
    public Object getItem(int i) {
        
        return null;
    }

    public String[] SpinnerPosition()
    {
        return SpinnerPosition;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_ma__listview, null);
        final TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        Spinner spinner= (Spinner) view.findViewById(R.id.spinner);



        final int temp=i;
        textView1.setText(EmpId[i]);
        ArrayAdapter<String> startColorsAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1,list);
        spinner.setAdapter(startColorsAdapter);

        if(EmpStatus[i].matches("Present"))
        {
            spinner.setSelection(0);
        }else if(EmpStatus[i].matches("Half-Day"))
        {
            spinner.setSelection(1);
        }else {
            spinner.setSelection(2);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {





                if (position == 0) {

                    SpinnerPosition[temp]="Present";
                    arg0.setBackgroundColor(Color.GREEN);
                } else if (position == 1) {
                    SpinnerPosition[temp]="Half-Day";
                    arg0.setBackgroundColor(Color.YELLOW);
                } else {
                    SpinnerPosition[temp]="Absent";
                    arg0.setBackgroundColor(Color.RED);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }
}
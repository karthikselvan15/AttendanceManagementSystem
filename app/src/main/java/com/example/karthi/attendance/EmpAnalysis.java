package com.example.karthi.attendance;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class EmpAnalysis extends AppCompatActivity {

    TextView textViewS,textViewE,textView1,textView2,textView3,textView4;
    Calendar mCurrentDate;
    int month,day,year;

    private SharedPreferences mPreferences;
    public String SDate,EDate;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_analysis);

        textViewS=(TextView) findViewById(R.id.textViewS);
        textViewE=(TextView) findViewById(R.id.textViewE);

        textView1=(TextView)findViewById(R.id.textView1);

        textView2=(TextView)findViewById(R.id.textView2);
        textView2.setTextColor(Color.GREEN);

        textView3=(TextView)findViewById(R.id.textView3);
        textView3.setTextColor(Color.YELLOW);

        textView4=(TextView)findViewById(R.id.textView4);
        textView4.setTextColor(Color.RED);



        mCurrentDate=Calendar.getInstance();
        day=mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month=mCurrentDate.get(Calendar.MONTH);
        year=mCurrentDate.get(Calendar.YEAR);

        textViewS.setText(year+"-"+(month+1)+"-"+day);
        textViewS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                DatePickerDialog datePickerDialog=new DatePickerDialog(EmpAnalysis.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        textViewS.setText(year+"-"+(month+1)+"-"+day);
                    }
                },year,month,day);
                datePickerDialog.show();


            }
        });

        textViewE.setText(year+"-"+(month+1)+"-"+day);
        textViewE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                DatePickerDialog datePickerDialog=new DatePickerDialog(EmpAnalysis.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        textViewE.setText(year+"-"+(month+1)+"-"+day);
                    }
                },year,month,day);
                datePickerDialog.show();


            }
        });


        mPreferences = getSharedPreferences("User", MODE_PRIVATE);
        final String id=mPreferences.getString("username",null);

        SDate=textViewS.getText().toString();
        EDate=textViewE.getText().toString();

        new analysis().execute(id,SDate,EDate);


        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDate=textViewS.getText().toString();
                EDate=textViewE.getText().toString();
                new analysis().execute(id,SDate,EDate);
            }
        });




        PieChart pieChart = (PieChart) findViewById(R.id.PieChart);
        pieChart.setUsePercentValues(true);

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(50f, 0));
        yvalues.add(new PieEntry(50f, 1));
        yvalues.add(new PieEntry(520f, 3));


    }


    private class analysis extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(EmpAnalysis.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("https://karthikselvann.000webhostapp.com/EmpAnalysis.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.getMessage();
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0])
                        .appendQueryParameter("sdate", params[1])
                        .appendQueryParameter("edate", params[2]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.getMessage();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();


            if (!result.equalsIgnoreCase("false"))

            {

                try {
                    JSONArray jsonarray = new JSONArray(result);

                    final String[] TempStatus = new String[jsonarray.length()];
                    int present = 0, halfday = 0, absent = 0, count = 0;
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        TempStatus[i] = jsonobject.getString("Status");
                        count += 1;
                        if (TempStatus[i].matches("Present")) {
                            present += 1;
                        } else if (TempStatus[i].matches("Half-Day")) {
                            halfday += 1;
                        } else {

                            absent += 1;
                        }


                    }


                    final PieChart pieChart = (PieChart) findViewById(R.id.PieChart);
                    pieChart.setUsePercentValues(true);

                    // IMPORTANT: In a PieChart, no values (Entry) should have the same
                    // xIndex (even if from different DataSets), since no values can be
                    // drawn above each other.
                    ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

                    yvalues.add(new PieEntry(present, 0));
                    yvalues.add(new PieEntry(halfday, 1));
                    yvalues.add(new PieEntry(absent, 2));

                    PieDataSet dataSet = new PieDataSet(yvalues, "Attendance");
                    PieData data = new PieData(dataSet);
                    // In Percentage term
                    data.setValueFormatter(new PercentFormatter());
                    // Default value
                    //data.setValueFormatter(new DefaultValueFormatter(0));
                    pieChart.setData(data);
                    pieChart.getDescription().setEnabled(false);

                    final int[] MY_COLORS = {Color.GREEN, Color.YELLOW, Color.RED};
                    ArrayList<Integer> colors = new ArrayList<Integer>();
                    for (int c : MY_COLORS) colors.add(c);

                    dataSet.setColors(colors);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setTransparentCircleRadius(50f);
                    pieChart.setHoleRadius(50f);

                    Legend leg = pieChart.getLegend();
                    leg.setEnabled(false);


                    data.setValueTextSize(13f);
                    data.setValueTextColor(Color.DKGRAY);
                    pieChart.animateXY(1400, 1400);


                    textView1.setText("" + count);
                    textView2.setText("" + present);
                    textView3.setText("" + halfday);
                    textView4.setText("" + absent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }else {


                final PieChart nopieChart = (PieChart) findViewById(R.id.PieChart);
                nopieChart.setUsePercentValues(true);

                // IMPORTANT: In a PieChart, no values (Entry) should have the same
                // xIndex (even if from different DataSets), since no values can be
                // drawn above each other.
                ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
                yvalues.add(new PieEntry(100f, 4));


                PieDataSet dataSet = new PieDataSet(yvalues, "Attendance");
                PieData data = new PieData(dataSet);
                // In Percentage term
                data.setValueFormatter(new PercentFormatter());
                // Default value
                //data.setValueFormatter(new DefaultValueFormatter(0));
                nopieChart.setData(data);
                nopieChart.getDescription().setEnabled(false);

                final int[] MY_COLORS = {Color.LTGRAY};
                ArrayList<Integer> colors = new ArrayList<Integer>();
                for (int c : MY_COLORS) colors.add(c);

                dataSet.setColors(colors);
                nopieChart.setDrawHoleEnabled(true);
                nopieChart.setTransparentCircleRadius(50f);
                nopieChart.setHoleRadius(50f);

                Legend leg = nopieChart.getLegend();
                leg.setEnabled(false);


                nopieChart.setDrawSliceText(false);
                nopieChart.getData().setDrawValues(false);
                data.setValueTextSize(13f);
                data.setValueTextColor(Color.DKGRAY);
                nopieChart.animateXY(1400, 1400);


                textView1.setText("-");
                textView2.setText("-");
                textView3.setText("-");
                textView4.setText("-");

            }
        }

    }
}

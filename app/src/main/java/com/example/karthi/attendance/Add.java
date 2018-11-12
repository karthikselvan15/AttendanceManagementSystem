package com.example.karthi.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class Add extends AppCompatActivity {

    private SharedPreferences mPreferences;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }
    public void add(View view) {
        EditText editText1=(EditText) findViewById(R.id.editText1);
        EditText editText2=(EditText) findViewById(R.id.editText2);
        EditText editText3=(EditText) findViewById(R.id.editText3);
        EditText editText4=(EditText) findViewById(R.id.editText4);
        EditText editText5=(EditText) findViewById(R.id.editText5);

        String empid=editText1.getText().toString();
        String empname=editText2.getText().toString();
        String empphone=editText3.getText().toString();
        String empemail=editText4.getText().toString();
        String emppass=editText5.getText().toString();



        int count=0;
        if (empid.isEmpty())
        {
            count=1;
            editText1.setError("Empty");
        }
        if (empname.isEmpty())
        {
            count=1;
            editText2.setError("Empty");
        }
        if (empphone.isEmpty())
        {
            count=1;
            editText3.setError("Empty");
        }
        if (empemail.isEmpty())
        {
            count=1;
            editText4.setError("Empty");
        }
        if (emppass.isEmpty())
        {
            count=1;
            editText5.setError("Empty");
        }
        if(count==0) {
            new Add.AsyncAdd().execute(empid, empname,empphone,empemail,emppass);
        }
    }



    private class AsyncAdd extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Add.this);
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

                url = new URL("https://karthikselvann.000webhostapp.com/Add.php");

                // Enter URL address where your php file resides


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

                mPreferences = getSharedPreferences("User", MODE_PRIVATE);
                String id=mPreferences.getString("username",null);
                String pass=mPreferences.getString("password",null);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", id)
                        .appendQueryParameter("password",pass)
                        .appendQueryParameter("empid", params[0])
                        .appendQueryParameter("empname", params[1])
                        .appendQueryParameter("empphone", params[2])
                        .appendQueryParameter("empemail", params[3])
                        .appendQueryParameter("emppass", params[4]);
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

            if(result.equalsIgnoreCase("true"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */


                Toast.makeText(Add.this, "Success", Toast.LENGTH_LONG).show();



            }else if (result.equalsIgnoreCase("false")){


                Toast.makeText(Add.this, "Something Wrong", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(Add.this, result, Toast.LENGTH_LONG).show();

            }
        }

    }
}

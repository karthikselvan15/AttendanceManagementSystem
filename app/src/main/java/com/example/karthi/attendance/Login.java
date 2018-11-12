package com.example.karthi.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {

    private SharedPreferences mPreferences;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;
    public int pc=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPreferences = getSharedPreferences("User", MODE_PRIVATE);
        final Intent employee = new Intent(this,Home.class);
        final Intent admin = new Intent(this,Admin.class);



        if (mPreferences.contains("username")) {
            // start Main activity
            String id=mPreferences.getString("username",null);
            String pass=mPreferences.getString("password",null);
            pc=1;
            new AsyncLogin().execute(id,pass);


        }

            final EditText editText1=(EditText) findViewById(R.id.editText1);
            final EditText editText2=(EditText) findViewById(R.id.editText2);
            Button create = (Button) findViewById(R.id.button);
            create.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String id=editText1.getText().toString();
                    String pass=editText2.getText().toString();
                    int count=0;
                    if (id.isEmpty())
                    {
                        count=1;
                        editText1.setError("Empty");
                    }
                    if (pass.isEmpty())
                    {
                        count=1;
                        editText2.setError("Empty");
                    }
                    if(count==0)
                    {

                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString("username", id);
                        editor.putString("password", pass);
                        editor.commit();
                        new AsyncLogin().execute(id,pass);

                    }



                }}
            );

        TextView textView=(TextView)findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forget=new Intent(getApplicationContext(),ForgetPassword.class);
                startActivity(forget);
                Login.this.finish();
            }
        });


    }



    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        private SharedPreferences mPreferences;
        ProgressDialog pdLoading = new ProgressDialog(Login.this);
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

                String id=params[0];
                id=id.toLowerCase();
                if(id.matches("admin"))
                {
                    url = new URL("https://karthikselvann.000webhostapp.com/AdminLogin.php");
                }
                else{
                    url = new URL("https://karthikselvann.000webhostapp.com/UserLogin.php");
                }
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

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
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


                mPreferences = getSharedPreferences("User", MODE_PRIVATE);
                String id=mPreferences.getString("username",null);
                id=id.toLowerCase();
                Intent intentAdmin = new Intent(Login.this,Admin.class);
                Intent intentHome = new Intent(Login.this,Home.class);
                if(id.matches("admin"))
                {
                    startActivity(intentAdmin);
                }
                else{
                    startActivity(intentHome);
                }

                Login.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                mPreferences = getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.commit();
                if(pc==0)
                {
                    Toast.makeText(Login.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Login.this, "Your Password has changed. Please log in again.", Toast.LENGTH_LONG).show();
                }

            } else {

                mPreferences = getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.commit();
                Toast.makeText(Login.this, "Something Wrong", Toast.LENGTH_LONG).show();

            }
        }

    }

}

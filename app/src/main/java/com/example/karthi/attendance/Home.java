package com.example.karthi.attendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String id=mPreferences.getString("username",null);

        Button button1=(Button)findViewById(R.id.button1);
        button1.setText("Profile\n("+id+")");

    }


    public void profile(View view) {
        Intent intent=new Intent(Home.this,Profile.class);
        startActivity(intent);
    }
    public void attendance(View view) {
        Intent intent=new Intent(Home.this,Attendance.class);
        startActivity(intent);
    }
    public void analysis(View view) {
        Intent intent=new Intent(Home.this,EmpAnalysis.class);
        startActivity(intent);
    }
    public void request(View view) {
        Intent intent=new Intent(Home.this,Request.class);
        startActivity(intent);
    }
    public void notification(View view) {
        Intent intent=new Intent(Home.this,Notification.class);
        startActivity(intent);
    }
    public void account(View view) {
        Intent intent=new Intent(Home.this,Account.class);
        startActivity(intent);
    }
    public void logout(View view) {



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure Logout");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences mPreferences = getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.commit();
                        Intent intent=new Intent(Home.this,Login.class);
                        startActivity(intent);
                        Home.this.finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
}

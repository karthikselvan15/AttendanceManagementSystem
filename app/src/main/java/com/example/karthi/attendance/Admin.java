package com.example.karthi.attendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void profile(View view) {
        Intent intent=new Intent(Admin.this,Profile.class);
        startActivity(intent);
    }
    public void list(View view) {
        Intent intent=new Intent(Admin.this,Employeelist.class);
        startActivity(intent);
    }
    public void add(View view) {
        Intent intent=new Intent(Admin.this,Add.class);
        startActivity(intent);
    }
    public void remove(View view) {
        Intent intent=new Intent(Admin.this,Remove.class);
        startActivity(intent);
    }
    public void markattendance(View view) {
        Intent intent=new Intent(Admin.this,MarkAttendance.class);
        startActivity(intent);
    }
    public void analysis(View view) {
        Intent intent=new Intent(Admin.this,Analysis.class);
        startActivity(intent);
    }
    public void message(View view) {
        Intent intent=new Intent(Admin.this,RequestMessage.class);
        startActivity(intent);
    }
    public void addnotification(View view) {
        Intent intent=new Intent(Admin.this,AddNotification.class);
        startActivity(intent);
    }
    public void account(View view) {
        Intent intent=new Intent(Admin.this,Account.class);
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
                        Intent intent=new Intent(Admin.this,Login.class);
                        startActivity(intent);
                        Admin.this.finish();
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

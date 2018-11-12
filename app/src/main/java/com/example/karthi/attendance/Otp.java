package com.example.karthi.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Otp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        final EditText  editText=(EditText)findViewById(R.id.otp);
        final String random=getIntent().getExtras().getString("random");
        final String user=getIntent().getExtras().getString("username");

        final Intent intent=new Intent(this,ChangePassword.class);

        intent.putExtra("username",user);

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp=editText.getText().toString();
                if(random.matches(otp))
                {

                    startActivity(intent);
                    Otp.this.finish();

                }
                else {

                    Toast.makeText(getApplicationContext(),"Invalid Otp", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

package com.example.dante.osoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VerifyActivity extends AppCompatActivity {
 Button verfy;
 TextView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        verfy=  findViewById(R.id.verify);
        welcome= (TextView) findViewById(R.id.welcome);
         Intent intent=getIntent();
         String name=intent.getStringExtra("fname");
         welcome.setText(name+",Your account has been created!");

        verfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Account.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
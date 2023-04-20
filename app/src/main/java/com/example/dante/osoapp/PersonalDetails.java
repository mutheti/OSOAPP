package com.example.dante.osoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class PersonalDetails extends AppCompatActivity {

    TextInputLayout lname,fname,phone;
    Button reg;
    String fn,ln,ph,em,pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        lname= (TextInputLayout) findViewById(R.id.lname);
        fname= (TextInputLayout) findViewById(R.id.fname);
        phone= (TextInputLayout) findViewById(R.id.phone);
        reg= (Button) findViewById(R.id.conti);
        Intent intent=getIntent();
        em=intent.getStringExtra("email");
        pas=intent.getStringExtra("password");
      reg.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(ValidateName() && ValidatePhone())
              {
                  Intent intent1=new Intent(getApplicationContext(),PersonDetails2.class);
                  intent1.putExtra("email",em);
                  intent1.putExtra("phone",ph);
                  intent1.putExtra("lname",ln);
                  intent1.putExtra("fname",fn);
                  intent1.putExtra("password",pas);
                  startActivity(intent1);
                  finish();
              }
          }
      });


    }
    private  boolean ValidatePhone(){
        ph=phone.getEditText().getText().toString().trim();
        if (ph.isEmpty())
        {
            phone.setError("Phone can't be empty");
            return false;
        }
        else if (ph.length()>10){
            phone.setError("Phone cant be greater than 10");
            return  false;
        }
        else if (ph.length()<10){
            phone.setError("Phone cant be less than 10 characters");
            return  false;
        }
        else{
            phone.setError(null);
            return true;
        }
    }
    private boolean ValidateName(){
        fn=fname.getEditText().getText().toString().trim();
        ln=lname.getEditText().getText().toString().trim();
        if(fn.isEmpty()){
            fname.setError("Can't be empty");
            return false;
        }
        else if(ln.isEmpty()){
            lname.setError("Can't be empty");
            return false;
        }
        else{
            lname.setError(null);
            fname.setError(null);
            return true;
        }
    }

}
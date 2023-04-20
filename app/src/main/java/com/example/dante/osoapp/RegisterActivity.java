package com.example.dante.osoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout email,password,confirm_password;
    Button reg;
    String em,pass,con_pass;
    private  static final Pattern PASSWORD_PATTERN=Pattern.compile("^"+
            "(?=.*[0-9])"+
            "(?=.*[a-zA-Z])"+
            "(?=\\S+$)"+
            ".{6,}"+
            "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email= (TextInputLayout) findViewById(R.id.email);
        password=  findViewById(R.id.password);
        confirm_password= (TextInputLayout) findViewById(R.id.confirm_password);
        reg= (Button) findViewById(R.id.conti);
        Intent intent=getIntent();
        em=intent.getStringExtra("email");
        email.getEditText().setText(em);
        email.setEnabled(false);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidatePassord()){
                    Intent intent1=new Intent(getApplicationContext(),PersonalDetails.class);
                    intent1.putExtra("email",em);
                    intent1.putExtra("password",pass);
                    startActivity(intent1);
                    finish();
                }

            }
        });
    }
    private boolean ValidatePassord(){
        pass=password.getEditText().getText().toString().trim();
        con_pass=confirm_password.getEditText().getText().toString().trim();
        if (pass.isEmpty() | con_pass.isEmpty()){
            password.setError("Password can't be empty");
            confirm_password.setError("Password can't be empty");
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(pass).matches()){
            password.setError("Weak Password");
            Toast.makeText(this, "Password should have atleast 8 characters 1\r ( capital letter, 1 numerical and a symbol(%,%,@, etc))", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (pass.length()<8)
        {
            password.setError("Password should have more than 8 characters");
            return false;
        } else if (!pass.equals(con_pass)) {
            confirm_password.setError("Password doesn't match");
        } else{
            password.setError(null);
            return true;
        }
        return false;
    }
}
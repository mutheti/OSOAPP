package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PersonDetails2 extends AppCompatActivity {

    TextInputLayout dob,address;
    Spinner spinnergender;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Button register;
    TextInputEditText dateo;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Query reference;
    ExtendedFloatingActionButton cancel,update;
    ProgressDialog progressDialog;
    String ln,fn,em,date,addr,ph,gen,pas;
    ArrayList<String> gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details2);
        spinnergender= (Spinner) findViewById(R.id.gender);
        register= (Button) findViewById(R.id.reg);
        dob= (TextInputLayout) findViewById(R.id.date1);
        address= (TextInputLayout) findViewById(R.id.address);
       db=FirebaseFirestore.getInstance();
       mAuth=FirebaseAuth.getInstance();

        dob.getEditText().setText("01/01/2023");
        gender=new ArrayList<>();

        gender.add("Male");
        gender.add("Female");


        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,gender);
        spinnergender.setAdapter(adapter);
        spinnergender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gen=gender.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(calendar.YEAR);
                int month=calendar.get(calendar.MONTH);
                int day=calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(PersonDetails2.this,onDateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });
        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=dayOfMonth+"/"+month+"/"+year;
               // dob.getEditText().setText(date);
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              GetData();
                mAuth.createUserWithEmailAndPassword(em, pas)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    UploadToFirestore();

                                } else {
                                    Toast.makeText(PersonDetails2.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PersonDetails2.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void GetData() {
        Intent intent=getIntent();
        ln=intent.getStringExtra("lname");
        fn=intent.getStringExtra("fname");
        em=intent.getStringExtra("email");
        date=dob.getEditText().getText().toString();
        ph=intent.getStringExtra("phone");
        pas=intent.getStringExtra("password");
        addr=address.getEditText().getText().toString();
        Toast.makeText(this, em, Toast.LENGTH_SHORT).show();

    }
    private void UploadToFirestore() {
        GetData();
        Map<String, Object> user = new HashMap<>();
        user.put("first", fn);
        user.put("last", ln);
        user.put("born", date);
        user.put("email",em);
        user.put("phone", ph);
        user.put("residence", addr);
        user.put("gender", gen);


        // Add a new document with a generated ID

        db.collection("user")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PersonDetails2.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),VerifyActivity.class);
                        intent.putExtra("fname",fn);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PersonDetails2.this, "Error Loading Your Data\n"+
                                e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
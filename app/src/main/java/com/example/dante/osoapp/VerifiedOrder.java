package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VerifiedOrder extends AppCompatActivity {
    Button verfy;
    TextView welcome;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_order);
        verfy=  findViewById(R.id.verify);
        welcome= (TextView) findViewById(R.id.welcome);
        Intent intent=getIntent();
        String name=intent.getStringExtra("fname");
      useremail();

        verfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void useremail(){
        Query ref=db.collection("user").whereEqualTo("email",mAuth.getCurrentUser().getEmail());
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot snap: queryDocumentSnapshots) {
                    String user = snap.getString("first");
                    welcome.setText(user+",Your order has been recieved!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerifiedOrder.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
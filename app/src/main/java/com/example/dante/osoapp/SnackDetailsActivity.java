package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SnackDetailsActivity extends AppCompatActivity {

    ImageView image;
    TextView name,price,description;
    int imagesnack;
    String name1,price1,descr,imge,email;
    ImageButton favourite,saved,cart;
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_details);
        Context context;
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        favourite= (ImageButton) findViewById(R.id.favourite);
        saved= (ImageButton) findViewById(R.id.saved);
        cart= (ImageButton) findViewById(R.id.cart);
        image= (ImageView) findViewById(R.id.item_image);
        name= (TextView) findViewById(R.id.item_name);
        price= (TextView) findViewById(R.id.item_price);
        description= (TextView) findViewById(R.id.item_details);
        Intent intent=getIntent();
       // image.setImageResource(intent.getStringExtra("image"));
       Glide.with(this).load(intent.getStringExtra("image")).into(image);
       imge=intent.getStringExtra("image");
        name.setText(intent.getStringExtra("name"));
        price.setText("Ksh. "+intent.getStringExtra("price"));
        description.setText(intent.getStringExtra("description"));


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);


            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             GetData();
             UserFavouriteList();
            }
        });
    }

    private void UserFavouriteList() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("iname", name1);
        user.put("idescription",descr);
        user.put("price",price1);
        user.put("image", imge);



        // Add a new document with a generated ID

        db.collection("Favourite")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SnackDetailsActivity.this, "Added to Favourite", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SnackDetailsActivity.this, "Error Loading Your Data\n"+
                                e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void GetData() {
        name1=name.getText().toString();
        price1=price.getText().toString();
        descr=description.getText().toString();
        name1=name.getText().toString();
        email=mAuth.getCurrentUser().getEmail();
    }

}
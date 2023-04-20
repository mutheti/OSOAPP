package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.StateListAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button userverif;
    TextInputLayout email;
    String em;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    
    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.nav_account);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        userverif= (Button) findViewById(R.id.conti);
        email= (TextInputLayout) findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        userverif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateEmail();
                CheckUserExists();

            }
        });

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.nav_order:

                        startActivity(new Intent(getApplicationContext(), Favourite.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;



                    case R.id.nav_account:


                        return true;

                }
                return false;
            }
        });

    }

    private void CheckUserExists() {
if(ValidateEmail()) {
    Query reference = db.collection("user").whereEqualTo("email", em);
    reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                             @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                 //Toast.makeText(LoginActivity.this, snap.getString("email").toString(), Toast.LENGTH_SHORT).show();
            for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                                                     String user;
                                                     user = snap.getString("email");
                                                     if (user.equals(em)) {
                                                         Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                        intent.putExtra("email",em);
                                                         startActivity(intent);
                                                         finish();

                                                     }
                                                     else{

                                                     }

                                                     // Toast.makeText(LoginActivity.this, "Not an Admin", Toast.LENGTH_SHORT).show();


                                                 }
                                             }

                                         }


    ).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

           Toast.makeText(AccountActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    });
    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
    intent.putExtra("email", em);
    startActivity(intent);
    finish();
}
    }

    private boolean ValidateEmail(){
        em=email.getEditText().getText().toString().trim();
        if(em.isEmpty()){
            email.setError("Email can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            email.setError("Enter a Valid Email Address");
            return false;
        }
        else
        {
            email.setError(null);
            return true;
        }
    }
}

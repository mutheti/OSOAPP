package com.example.dante.osoapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
   TextInputLayout email,password;
   Button login;
   ProgressDialog progressDialog;
   FirebaseAuth mAuth;
   String ema,pass;
   FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login= (Button) findViewById(R.id.login);
        email= (TextInputLayout) findViewById(R.id.email);
        password= (TextInputLayout) findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();
        progressDialog =new ProgressDialog(this);
       progressDialog.setTitle("Login");
       progressDialog.setMessage("Authenticating...");
        Intent intent=getIntent();
        email.setEnabled(false);
        ema=intent.getStringExtra("email");
        email.getEditText().setText(intent.getStringExtra("email"));
       pass=password.getEditText().getText().toString();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();
                if(!pass.isEmpty()) {
                    GetData();
                    Authenticate(ema, pass);
                }
                else {
                    password.setError("password can' t be empty");
                }
            }
        });
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               Intent intent1=new Intent(getApplicationContext(),AccountActivity.class);
               startActivity(intent1);
               finish();
            }
        };

    }

    private void GetData() {
      ema=email.getEditText().getText().toString();
      pass=password.getEditText().getText().toString();
    }
   private void ResetPassword(){
       mAuth.getCurrentUser().sendEmailVerification();
       mAuth.sendPasswordResetEmail("em");
    }
    private void Authenticate(String em,String pas) {

        progressDialog.show();
        mAuth.signInWithEmailAndPassword(em, pas)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.setMessage("Logging....");
                        if (task.isSuccessful()) {
                            Query reference = db.collection("admin").whereEqualTo("email",em);
                            reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                     @Override
                                                                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                         //Toast.makeText(LoginActivity.this, snap.getString("email").toString(), Toast.LENGTH_SHORT).show();
                                                                         for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                                                                            String user_Admin=snap.getString("email");
                                                                             if(user_Admin.equals(em)) {
                                                                                 Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                                                                 startActivity(intent);
                                                                                 finish();
                                                                             }

                                                                             // Toast.makeText(LoginActivity.this, "Not an Admin", Toast.LENGTH_SHORT).show();


                                                                         }
                                                                     }

                                                                 }


                            );
                            Intent intent = new Intent(getApplicationContext(), Account.class);
                            startActivity(intent);
                            finish();
                        }

                        else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Invalid Login Credentials.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                })
.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
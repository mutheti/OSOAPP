package com.example.dante.osoapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class WelcomingWindow extends AppCompatActivity {

    ProgressBar progressBar;
    ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcoming_window);

        Init();
        progressBar.setScrollBarFadeDuration(300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        },3000);
    }
    private void Init(){

        progressBar=findViewById(R.id.prog);
        objectAnimator=ObjectAnimator.ofInt(progressBar,"progress",0,100);
    }
}
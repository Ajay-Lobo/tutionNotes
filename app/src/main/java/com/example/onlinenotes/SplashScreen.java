package com.example.onlinenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar loaderProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loaderProgressBar = findViewById(R.id.loaderProgressBar);
        loaderProgressBar.setVisibility(View.INVISIBLE); // Use the correct View class here

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show the ProgressBar to indicate loading
                loaderProgressBar.setVisibility(View.VISIBLE);

                // Delay for a short duration to show the ProgressBar
                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Move to the LoginActivity
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Finish the splash activity to prevent going back to it
                    }
                }, 2000); // 1 second delay
            }
        }, 1000); // 3 seconds delay
    }
}
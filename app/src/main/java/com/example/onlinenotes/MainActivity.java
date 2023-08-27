package com.example.onlinenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
     EditText usernameEditText,passwordEditText;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextDate);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get username and password from EditTexts
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Perform login validation (example: check against hardcoded credentials)
                if (isValidCredentials(username, password)) {
                    navigateToDashboard();
                    // Successful login action (e.g., navigate to another activity)
                } else {
                    // Handle unsuccessful login (e.g., show an error message)
                }
            }

            private boolean isValidCredentials(String username, String password) {
                return username.equals("your_username") && password.equals("your_password");
            }
        });
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(MainActivity.this, .class);
        startActivity(intent);
        finish();
    }
}

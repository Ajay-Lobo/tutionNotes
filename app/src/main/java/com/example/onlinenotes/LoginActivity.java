package com.example.onlinenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText,passwordEditText;
    Button loginButton;
    TextView RegisterLink;

    Database1 database1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.editTextName2);
        passwordEditText = findViewById(R.id.editTextPass3);
        loginButton = findViewById(R.id.loginButton);
        RegisterLink = findViewById(R.id.LoginView2);
        // database calling
        database1 = new Database1(getApplicationContext());

//pressing the register now
        RegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                //Returning user role from database
                int userRole = database1.getUserRole(username, password);
                if (userRole == 0) {
                    // Redirect to teacher dashboard
                    Intent intent = new Intent(LoginActivity.this, TeacherDashboard.class);
                    startActivity(intent);
                } else if (userRole == 1) {
                    // Redirect to student dashboard
                    Intent intent = new Intent(LoginActivity.this, StudentDashboard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                    // Clear the username and password fields
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                }
            }
        });



    }
}
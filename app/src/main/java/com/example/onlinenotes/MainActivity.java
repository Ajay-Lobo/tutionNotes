package com.example.onlinenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
     EditText usernameEditText,passwordEditText1,passwordEditText2,name1;
     TextView loginPress;
    Button loginButton;
    Database1 database1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name1 = findViewById(R.id.editTextText);
        usernameEditText= findViewById(R.id.editTextName);
        passwordEditText1 = findViewById(R.id.editTextPass1);
        passwordEditText2 = findViewById(R.id.editTextPass2);
        loginPress = findViewById(R.id.LoginView2);

        loginButton = findViewById(R.id.loginButton);
//        database calling
        database1 = new Database1(getApplicationContext());
        //linking another page
        loginPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get username and password from EditTexts
                String name = name1.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText1.getText().toString();
                String confirmPassword = passwordEditText2.getText().toString();
                    if(name.length()<4) {
                        //error messages
                        Toast.makeText(getApplicationContext(),"Name is Short",Toast.LENGTH_SHORT).show();
                    }
                    else if(password.length()<6){
                        Toast.makeText(getApplicationContext(),"Password is Short",Toast.LENGTH_SHORT).show();

                    }
               else if(!password.equalsIgnoreCase(confirmPassword)){
                        Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_SHORT).show();
                    }
               else {
                        if (database1.registerData(name, username, password)) {
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                            navigateToLogin();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Registered Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    }


                // Perform login validation (example: check against hardcoded credentials)



        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

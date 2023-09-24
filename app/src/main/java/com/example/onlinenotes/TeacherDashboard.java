package com.example.onlinenotes;

import static com.example.onlinenotes.R.id.editButton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherDashboard extends AppCompatActivity {
    Button addButton,editButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        addButton = findViewById(R.id.button);
        editButton = findViewById(R.id.editButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherDashboard.this, AddNotes.class);
                startActivity(intent);

            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherDashboard.this, DisplayNotes.class);
                startActivity(intent);

            }
        });
    }
}
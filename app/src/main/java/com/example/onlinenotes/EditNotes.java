package com.example.onlinenotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditNotes extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonSaveOrUpdate;

    private Database1 databaseHelper; // Your database helper class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSaveOrUpdate = findViewById(R.id.buttonSaveOrUpdate);

        // Initialize the database helper
        databaseHelper = new Database1(this);

        // Retrieve the passed values from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");

            // Set the values to the EditText fields
            editTextTitle.setText(title);
            editTextDescription.setText(description);
        }

        // Set a click listener for the Save or Update button
        buttonSaveOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the updated title and description from the EditText fields
                String updatedTitle = editTextTitle.getText().toString();
                String updatedDescription = editTextDescription.getText().toString();

                // Update the database with the new values
                if (updateNoteInDatabase(updatedTitle, updatedDescription)) {
                    // Update successful
                    Toast.makeText(EditNotes.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditNotes.this, TeacherDashboard.class);
                    startActivity(intent);
                    finish();

                } else {
                    // Update failed
                    Toast.makeText(EditNotes.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                }

                // Finish the activity to go back to the previous screen

            }
        });
    }

    // Method to update the note in the database
    private boolean updateNoteInDatabase(String updatedTitle, String updatedDescription) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Define the new values to be updated
        ContentValues values = new ContentValues();
        values.put("title", updatedTitle);
        values.put("description", updatedDescription);

        // Define the WHERE clause to identify the note to be updated (e.g., using note_id)
        String whereClause = "title = ?"; // You may want to use a unique identifier

        // Define the WHERE clause arguments
        String[] whereArgs = { updatedTitle }; // Match the title to identify the note

        // Perform the update operation
        int rowsUpdated = db.update("upload", values, whereClause, whereArgs);

        // Close the database connection
        db.close();

        // Return true if at least one row was updated
        return rowsUpdated > 0;
    }
}

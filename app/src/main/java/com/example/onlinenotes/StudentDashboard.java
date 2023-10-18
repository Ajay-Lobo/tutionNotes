package com.example.onlinenotes;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.widget.Toast;
        import com.example.onlinenotes.Database1;

public class StudentDashboard extends AppCompatActivity {
    private Database1 databaseHelper;
    private RecyclerView recyclerView;
    private NoteAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the database helper
        databaseHelper = new Database1(this);

        // Fetch notes from the database
        Cursor cursor = databaseHelper.getNotesInfo();

        // Check if there are any notes in the cursor
        if (cursor.getCount() == 0) {
            // No notes available, display a message
            Toast.makeText(this, "No notes available.", Toast.LENGTH_SHORT).show();
        } else {
            // Create an adapter and set it to the RecyclerView
            adapter = new NoteAdapter2(cursor,this);
            recyclerView.setAdapter(adapter);
        }
    }
}

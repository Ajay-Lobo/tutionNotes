package com.example.onlinenotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinenotes.Database1;
import com.example.onlinenotes.EditNotes;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    private Cursor cursor;

    public NoteAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            holder.textViewTitle.setText(title);
            holder.textViewDescription.setText(description);

            // Set click listeners for the delete and edit buttons
            holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle delete button click
                    // Get the note's ID or other identifier
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    // Call a method to delete the note from the database
                    deleteNoteFromDatabase(title);
                }
            });

            holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle edit button click
                    // Pass the note's ID or other identifier to the edit activity
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                    // Pass the title and description to the EditNotes activity
                    Intent intent = new Intent(context, EditNotes.class);
                    // intent.putExtra("note_id", noteId); // Pass the note ID if needed
                    intent.putExtra("title", title);
                    intent.putExtra("description", description);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        Button buttonDelete;
        Button buttonEdit;

        public NoteViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }

    private void deleteNoteFromDatabase(String title) {
        // Get an instance of your database helper class
        Database1 databaseHelper = new Database1(context);

        // Get a writable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Define the selection and selectionArgs for the DELETE statement
        String selection = "title = ?";
        String[] selectionArgs = { title };

        // Execute the DELETE statement
        int deletedRows = db.delete("upload", selection, selectionArgs);

        // Close the database connection
        db.close();

        // Check if the deletion was successful
        if (deletedRows > 0) {
            // The note was deleted successfully
            // Display a toast message
            Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the TeacherDashboard activity
            Intent intent = new Intent(context, TeacherDashboard.class);
            context.startActivity(intent);

            // Finish the current activity (assuming this is not the TeacherDashboard)
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        } else {
            // Deletion failed or no rows were deleted
            // Display an error message
            Toast.makeText(context, "Failed to delete note", Toast.LENGTH_SHORT).show();
        }

        // Notify the RecyclerView that the data has changed
        notifyDataSetChanged();
    }




}

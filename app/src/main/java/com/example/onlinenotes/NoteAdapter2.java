package com.example.onlinenotes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter2 extends RecyclerView.Adapter<NoteAdapter2.NoteViewHolder> {
    private Cursor cursor;
    private Context context;

    public NoteAdapter2(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_notes, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            byte[] fileDataBlob = cursor.getBlob(cursor.getColumnIndexOrThrow("fileData"));

            holder.textViewTitle.setText(title);
            holder.textViewDescription.setText(description);

            // Detect and display the appropriate data type (PDF or image)
            if (isPDF(fileDataBlob)) {
                holder.imageViewFile.setImageResource(R.drawable.plusbutton); // Set a PDF icon or use your own
            } else if (isImage(fileDataBlob)) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(fileDataBlob, 0, fileDataBlob.length);
                holder.imageViewFile.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        ImageView imageViewFile;

        public NoteViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle1);
            textViewDescription = itemView.findViewById(R.id.textViewDescription1);
            imageViewFile = itemView.findViewById(R.id.imageViewFile); // Add an ImageView in your view_notes.xml
        }
    }

    // Helper method to check if the data is a PDF
    private boolean isPDF(byte[] data) {
        // Implement your PDF detection logic here (e.g., by checking file headers)
        // Return true if it's a PDF, false otherwise
        return false;
    }

    // Helper method to check if the data is an image
    private boolean isImage(byte[] data) {
        // Implement your image detection logic here (e.g., by checking file headers)
        // Return true if it's an image, false otherwise
        return true;
    }
}

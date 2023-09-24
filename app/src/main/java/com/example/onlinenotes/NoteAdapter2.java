package com.example.onlinenotes;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter2 extends RecyclerView.Adapter<NoteAdapter2.NoteViewHolder> {
    private Cursor cursor;

    public NoteAdapter2(Cursor cursor) {
        this.cursor = cursor;
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
            holder.textViewTitle.setText(title);
            holder.textViewDescription.setText(description);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;

        public NoteViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle1);
            textViewDescription = itemView.findViewById(R.id.textViewDescription1);
        }
    }
}

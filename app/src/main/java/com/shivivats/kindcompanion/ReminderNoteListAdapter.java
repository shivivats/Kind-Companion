package com.shivivats.kindcompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReminderNoteListAdapter extends RecyclerView.Adapter<ReminderNoteListAdapter.NoteViewHolder> {

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private final CardView noteCardView;
        private final TextView noteTitleView;
        private final TextView noteBodyView;

        private NoteViewHolder(View noteView) {
            super(noteView);
            noteCardView = noteView.findViewById(R.id.noteCardView);
            noteTitleView = noteView.findViewById(R.id.noteTitleView);
            noteBodyView = noteView.findViewById(R.id.noteBodyView);
        }
    }

    private final LayoutInflater inflater;
    private List<NoteTuple> reminderNotesList; // cached copy of words

    ReminderNoteListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = inflater.inflate(R.layout.item_note_recyclerview, parent, false);
        return new NoteViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (reminderNotesList != null) {
            NoteTuple current = reminderNotesList.get(position);
            holder.noteTitleView.setText(current.noteTitle);
            holder.noteBodyView.setText(current.noteBody);
        } else {
            // covers the case of data not being ready yet
            holder.noteTitleView.setText("No Title");
            holder.noteBodyView.setText("No Body");
        }
    }

    void setNotes(List<NoteTuple> notes) {
        reminderNotesList = notes;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // reminderNotesList has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (reminderNotesList != null)
            return reminderNotesList.size();
        else
            return 0;
    }
}

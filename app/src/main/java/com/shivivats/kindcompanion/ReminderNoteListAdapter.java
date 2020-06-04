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

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final CardView noteCardView;
        private final TextView noteTitleView;
        private final TextView noteBodyView;

        public long currentNoteId;

        private NoteViewHolder(View noteView, long noteId) {
            super(noteView);
            noteCardView = noteView.findViewById(R.id.noteCardView);
            noteTitleView = noteView.findViewById(R.id.noteTitleView);
            noteBodyView = noteView.findViewById(R.id.noteBodyView);
            currentNoteId=noteId;
        }

        public void setData(NoteEntity note) {

        }


        @Override
        public void onClick(View view) {

        }
    }

    private final LayoutInflater inflater;
    private List<NoteEntity> reminderNotesList; // cached copy of words

    ReminderNoteListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = inflater.inflate(R.layout.item_note_recyclerview, parent, false);
        return new NoteViewHolder(noteView,-1);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (reminderNotesList != null) {
            NoteEntity current = reminderNotesList.get(position);
            holder.noteTitleView.setText(current.noteTitle);
            holder.noteBodyView.setText(current.noteBody);
            holder.currentNoteId = current.noteId;
        } else {
            // covers the case of data not being ready yet
            holder.noteTitleView.setText("No Title");
            holder.noteBodyView.setText("No Body");
            holder.currentNoteId = -1;
        }
    }

    void setNotes(List<NoteEntity> notes) {
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

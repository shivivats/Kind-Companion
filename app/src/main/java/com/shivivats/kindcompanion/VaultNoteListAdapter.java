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

public class VaultNoteListAdapter extends RecyclerView.Adapter<VaultNoteListAdapter.NoteViewHolder> {

    private final LayoutInflater inflater;
    private List<NoteEntity> vaultNotesList; // cached copy of words
    private NoteListClickListener noteListClickListener;

    VaultNoteListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setNoteListClickListener(NoteListClickListener listener) {
        noteListClickListener = listener;
    }

    @NonNull
    @Override
    public VaultNoteListAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = inflater.inflate(R.layout.item_note_recyclerview, parent, false);
        return new VaultNoteListAdapter.NoteViewHolder(noteView, -1);
    }

    @Override
    public void onBindViewHolder(@NonNull VaultNoteListAdapter.NoteViewHolder holder, int position) {
        if (vaultNotesList != null) {
            NoteEntity current = vaultNotesList.get(position);
            holder.noteTitleView.setText(current.noteTitle);
            holder.noteBodyView.setText(current.noteBody);
            holder.currentNoteId = current.noteId;
            if (current.noteImagesCount == 1) {
                holder.noteImageCountView.setText(current.noteImagesCount + " image");
            } else {
                holder.noteImageCountView.setText(current.noteImagesCount + " images");
            }
            if (current.noteAudioCount == 1) {
                holder.noteAudioCountView.setText(current.noteAudioCount + " audio");
            } else {
                holder.noteAudioCountView.setText(current.noteAudioCount + " audios");
            }
        } else {
            // covers the case of data not being ready yet
            holder.noteTitleView.setText(R.string.noteNoTitle);
            holder.noteBodyView.setText(R.string.noteNoBody);
            holder.currentNoteId = -1;
            holder.noteImageCountView.setText("-1");
            holder.noteAudioCountView.setText("-1");
        }
    }

    // getItemCount() is called many times, and when it is first called,
    // vaultNotesList has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (vaultNotesList != null)
            return vaultNotesList.size();
        else
            return 0;
    }

    void setNotes(List<NoteEntity> notes) {
        vaultNotesList = notes;
        notifyDataSetChanged();
    }

    public List<NoteEntity> getVaultNotesList() {
        return vaultNotesList;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardView noteCardView;
        private final TextView noteTitleView;
        private final TextView noteBodyView;
        private final TextView noteImageCountView;
        private final TextView noteAudioCountView;

        public long currentNoteId;

        public NoteViewHolder(View noteView, long noteId) {
            super(noteView);
            noteCardView = noteView.findViewById(R.id.noteCardView);
            noteTitleView = noteView.findViewById(R.id.noteTitleView);
            noteBodyView = noteView.findViewById(R.id.noteBodyView);
            noteImageCountView = noteView.findViewById(R.id.noteImageCountView);
            noteAudioCountView = noteView.findViewById(R.id.noteAudioCountView);
            currentNoteId = noteId;

            noteView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (noteListClickListener != null) {
                noteListClickListener.onNoteListItemClicked(view, getAdapterPosition());
            }
        }
    }
}

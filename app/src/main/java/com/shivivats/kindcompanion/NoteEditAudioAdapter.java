package com.shivivats.kindcompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoteEditAudioAdapter extends RecyclerView.Adapter<NoteEditAudioAdapter.NoteAudioViewHolder> {

    private final LayoutInflater inflater;
    private List<AudioEntity> audios; // cached copy of audios
    private NoteEditAudioClickListener noteEditAudioClickListener;

    NoteEditAudioAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setNoteEditAudioClickListener(NoteEditAudioClickListener listener) {
        noteEditAudioClickListener = listener;
    }

    @NotNull
    @Override
    public NoteEditAudioAdapter.NoteAudioViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_note_entry_audio, parent, false);
        return new NoteEditAudioAdapter.NoteAudioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull NoteEditAudioAdapter.NoteAudioViewHolder holder, int position) {
        if (audios != null) {
            AudioEntity current = audios.get(position);
            // holder.imageView.setImageURI(current.imageUri);
            // SET THE AUDIO THING HERE

        } else {
            // holder.imageView.setImageBitmap(null);
            // NULL case here
        }
    }

    // getItemCount() is called many times, and when it is first called,
    // audio list has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (audios != null)
            return audios.size();
        else return 0;
    }

    void setAudios(List<AudioEntity> audioList) {
        audios = audioList;
        notifyDataSetChanged();
    }

    public List<AudioEntity> getAudiosList() {
        return audios;
    }

    class NoteAudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardView cardView;
        private final ImageView playPauseImage;
        private final SeekBar seekBar;
        private final LinearLayout playLayout;

        private NoteAudioViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.audioCardView);
            playPauseImage = itemView.findViewById(R.id.audioCardImagePlayPause);
            seekBar = itemView.findViewById(R.id.audioCardSeekBar);
            playLayout = itemView.findViewById(R.id.audioCardPlayLayout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (noteEditAudioClickListener != null) {
                noteEditAudioClickListener.onNoteEditAudioClicked(view, getAdapterPosition());
            }
        }
    }
}

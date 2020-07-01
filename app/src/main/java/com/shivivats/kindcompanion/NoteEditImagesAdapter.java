package com.shivivats.kindcompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteEditImagesAdapter extends RecyclerView.Adapter<NoteEditImagesAdapter.NoteImagesViewHolder> {

    private final LayoutInflater inflater;
    private List<ImageEntity> images; // cached copy of images
    private NoteEditImagesClickListener noteEditImagesClickListener;

    NoteEditImagesAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setNoteEditImagesClickListener(NoteEditImagesClickListener listener) {
        noteEditImagesClickListener = listener;
    }

    @Override
    public NoteImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_note_entry_image, parent, false);
        return new NoteImagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteImagesViewHolder holder, int position) {
        if (images != null) {
            ImageEntity current = images.get(position);
            holder.imageView.setImageURI(current.imageUri);

        } else {
            holder.imageView.setImageBitmap(null);
        }
    }

    // getItemCount() is called many times, and when it is first called,
    // images list has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (images != null)
            return images.size();
        else return 0;
    }

    void setImages(List<ImageEntity> imagesList) {
        images = imagesList;
        notifyDataSetChanged();
    }

    public List<ImageEntity> getImagesList() {
        return images;
    }

    class NoteImagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardView cardView;
        private final ImageView imageView;

        private NoteImagesViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.imageCardView);
            imageView = itemView.findViewById(R.id.imageViewItem);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (noteEditImagesClickListener != null) {
                noteEditImagesClickListener.onNoteEditImagesClicked(view, getAdapterPosition());
            }
        }
    }
}

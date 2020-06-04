package com.shivivats.kindcompanion;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteImagesAdapter extends RecyclerView.Adapter<NoteImagesAdapter.NoteImagesViewHolder> {

    class NoteImagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final CardView cardView;
        private final ImageView imageView;

        private NoteImagesViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.imageCardView);
            imageView = itemView.findViewById(R.id.imageViewItem);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private final LayoutInflater inflater;
    private List<ImageEntity> images; // cached copy of images

    NoteImagesAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NoteImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_note_entry_image, parent, false);
        return new NoteImagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteImagesViewHolder holder, int position) {
        if(images!=null) {
            ImageEntity current = images.get(position);
            holder.imageView.setImageURI(current.imageUri);
        }else {
            holder.imageView.setImageBitmap(null);
        }
    }

    void setImages(List<ImageEntity> imagesList) {
        images=imagesList;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // images list has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (images != null)
            return images.size();
        else return 0;
    }
}

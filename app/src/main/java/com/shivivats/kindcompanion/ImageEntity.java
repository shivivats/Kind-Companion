package com.shivivats.kindcompanion;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Note_Images", foreignKeys = @ForeignKey(entity = NoteEntity.class,
        parentColumns = "note_id",
        childColumns = "image_note_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class ImageEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    public long imageId;

    @ColumnInfo(name = "image_uri", defaultValue = "")
    public Uri imageUri;

    @ColumnInfo(name = "is_drawing")
    public boolean isDrawing;

    @ColumnInfo(name = "image_note_id")
    public long imageNoteId;
}

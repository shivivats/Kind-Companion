package com.shivivats.kindcompanion;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Note_Audio",
        foreignKeys = @ForeignKey(entity = NoteEntity.class,
                parentColumns = "note_id",
                childColumns = "audio_note_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
        indices = {@Index(value = {"audio_note_id"})})
public class AudioEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "audio_id")
    public long audioId;

    @ColumnInfo(name = "audio_uri", defaultValue = "")
    public Uri audioUri;

    @ColumnInfo(name = "is_recording")
    public boolean isRecording;

    @ColumnInfo(name = "audio_note_id")
    public long audioNoteId;
}

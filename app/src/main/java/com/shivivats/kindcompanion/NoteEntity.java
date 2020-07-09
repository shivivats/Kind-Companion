package com.shivivats.kindcompanion;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    public long noteId;

    /*
    WHEREVER WE USE NOTE_TYPE WE NEED TO USE IT LIKE THIS:

    To Assign:
    NoteType var = NoteType.NOTE_REMINDER;
    int valueOfNoteType = var.getValue();

    To Get Value From Int:
    NoteType noteType = NoteType.fromInt(0)

    So we will store the int value we initially get, however we will convert the value back to enum whenever we need to process it.
     */
    @ColumnInfo(name = "note_type")
    public int noteType;

    @ColumnInfo(name = "note_title", defaultValue = "")
    public String noteTitle;

    @ColumnInfo(name = "note_body", defaultValue = "")
    public String noteBody;

    @ColumnInfo(name = "note_images_count", defaultValue = "0")
    public int noteImagesCount;

    @ColumnInfo(name = "note_audio_count", defaultValue = "0")
    public int noteAudioCount;
}

package com.shivivats.kindcompanion;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="note_id")
    public int noteId;

    @ColumnInfo(name="note_type")
    public NoteType noteType;

    @ColumnInfo(name = "note_title", defaultValue = "")
    public String noteTitle;

    @ColumnInfo(name = "note_body", defaultValue = "")
    public String noteBody;
}

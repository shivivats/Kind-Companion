package com.shivivats.kindcompanion;

import androidx.room.ColumnInfo;

public class NoteTuple {
    @ColumnInfo(name = "note_title")
    public String noteTitle;

    @ColumnInfo(name = "note_body")
    public String noteBody;
}

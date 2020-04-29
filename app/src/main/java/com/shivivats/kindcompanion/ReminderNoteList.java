package com.shivivats.kindcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

public class ReminderNoteList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_note_list);

        NoteDatabase noteDB = NoteDatabase.getNoteDatabase(this);

        noteDB.noteEntityDao().insertNotes();

    }
}

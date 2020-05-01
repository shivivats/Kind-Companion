package com.shivivats.kindcompanion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ReminderNoteEntryActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.shivivats.kindcompanion.";

    EditText noteTitle;
    EditText noteBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_note_entry);

        // we basically just set the toolbar as the support action bar so we can get it using a function anywhere in the activity now
        Toolbar reminderEntryHeaderBar = findViewById(R.id.reminderNoteEntryTopBar);
        setSupportActionBar(reminderEntryHeaderBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        ab.setDisplayShowTitleEnabled(false);

        // set the views
        noteTitle = findViewById(R.id.reminderNoteEntryTitleField);
        noteBody = findViewById(R.id.reminderNoteEntryTextField);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_reminder_note_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_reminder_note:
                StoreReminder();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                // We dont need to handle the "up/back" button here bc its gonna default to the superclass
                return super.onOptionsItemSelected(item);
        }
    }

    private void StoreReminder() {
        // here we do the intent stuff
        Intent replyIntent = new Intent();
        String nt = noteTitle.getText().toString();
        String nb = noteBody.getText().toString();
        //NoteEntity noteEntity= new NoteEntity();
        //noteEntity.noteType=NoteType.NOTE_REMINDER.getValue();
        //noteEntity.noteTitle=nt;
        //noteEntity.noteBody=nb;
        replyIntent.putExtra("noteTitle", nt);
        replyIntent.putExtra("noteBody", nb);
        replyIntent.putExtra("noteType", NoteType.NOTE_REMINDER.getValue());
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}

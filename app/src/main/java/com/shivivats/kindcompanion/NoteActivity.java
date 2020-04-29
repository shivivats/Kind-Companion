package com.shivivats.kindcompanion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

// Shivi's Notes:
//  I might need a ViewModel for this, but I'm not implementing one right now since the activity is pretty simple.

public class NoteActivity extends AppCompatActivity {

    EditText noteTitle;
    EditText noteBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FOR FUTURE CONSIDERATION
        // or i can implement the onRestoreInstanceState method
        /*
        if (savedInstanceState != null) {
            // Restore value of members from saved state
        } else {
            // Probably initialize members with default values for a new instance
        }
        */

        setContentView(R.layout.note_activity);

        // we basically just set the toolbar as the support action bar so we can get it using a function anywhere in the activity now
        Toolbar noteEntryHeaderBar = findViewById(R.id.noteEntryTopBar);
        setSupportActionBar(noteEntryHeaderBar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        ab.setDisplayShowTitleEnabled(false);

        // set the views
        noteTitle = findViewById(R.id.noteEntryTitleField);
        noteBody = findViewById(R.id.noteEntryTextField);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_entry_topbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_store_note:
                StoreNote();
                return true;

            case R.id.action_store_vault:
                StoreVault();
                return true;

            case R.id.action_send_void:
                SendVoid();
                return true;

            case R.id.action_burn_thoughts:
                BurnThoughts();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                // We dont need to handle the "up/back" button here bc its gonna default to the superclass
                return super.onOptionsItemSelected(item);
        }
    }

    private void StoreNote() {
        // we store the note here, should learn about storage now
        // we should probably use a service for managing storage

        /*
        String fileData, filename;
        fileData = noteBody.getText().toString();
        filename = noteTitle.getText().toString();
        try {
            FileOutputStream fOut = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            fOut.write(fileData.getBytes());
            fOut.close();
            Toast.makeText(getBaseContext(), "file saved", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        */
    }

    private void StoreVault() {

    }

    private void SendVoid() {

    }

    private void BurnThoughts() {

    }
}

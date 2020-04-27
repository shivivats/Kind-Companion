package com.example.kindcompanion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FOR FUTURE CONSIDERATION
        // or i can implement the onRestoreInstanceState method
        if (savedInstanceState != null) {
            // Restore value of members from saved state
        } else {
            // Probably initialize members with default values for a new instance
        }
        setContentView(R.layout.note_activity);

        // we basically just set the toolbar as the support action bar so we can get it using a function anywhere in the activity now
        Toolbar noteEntryHeaderBar = (Toolbar) findViewById(R.id.noteEntryHeaderBar);
        setSupportActionBar(noteEntryHeaderBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_entry_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.test_action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.test_action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                // WE dont need to handle the "up/back" button here bc its gonna default to the superclass
                return super.onOptionsItemSelected(item);

        }
    }
}

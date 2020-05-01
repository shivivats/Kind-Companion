package com.shivivats.kindcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class FrontPageActivity extends AppCompatActivity {

    SeekBar textScaleSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        SeekBarSetup();
    }

    public void OnClickNewNote(View view) {
        Intent newNoteIntent = new Intent(this, NoteEntryActivity.class);
        //Log.d("FrontPageActivity", "New Note Button Intent");
        startActivity(newNoteIntent);
    }

    private void SeekBarSetup() {
        textScaleSeekbar = findViewById(R.id.seekBar);
        textScaleSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // here we would update the text size
                //GlobalAppVariables.setTextScale();
                Toast.makeText(getApplicationContext(), progress + " progress", Toast.LENGTH_SHORT).show();
                // progress is output as an integer ranging from 0 to 3
                // so we can just use progress+1
                // text scale should be like: multiply the default text value by the progress bar number?
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // idk if we should wait for the user to stop updating the seekbar before updating the text size
                // probably not
            }
        });
    }

    public void OnClickReminders(View view) {
        Intent reminderIntent = new Intent(this, ReminderNoteListActivity.class);
        startActivity(reminderIntent);
    }
}


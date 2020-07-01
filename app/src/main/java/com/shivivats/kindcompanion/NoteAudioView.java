package com.shivivats.kindcompanion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class NoteAudioView extends AppCompatActivity {

    private long currentAudioId;

    private Uri currentAudioUri;

    private ImageView playPauseImage;
    private SeekBar seekBar;
    private LinearLayout playLayout;

    private Toolbar noteAudioViewTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_audio_view);

        playPauseImage = findViewById(R.id.audioViewImagePlayPause);
        seekBar = findViewById(R.id.audioViewSeekBar);
        playLayout = findViewById(R.id.audioViewPlayLayout);

        noteAudioViewTopBar = findViewById(R.id.audioViewTopBar);

        setSupportActionBar(noteAudioViewTopBar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        ab.setDisplayShowTitleEnabled(false);

        Intent intent  = getIntent();
        currentAudioId = intent.getLongExtra("AUDIO_ID", -1);

        if(currentAudioId==-1) {
            setResult(-2);
            finish();
        }

        currentAudioUri = Uri.parse(intent.getStringExtra("AUDIO_URI"));

        //imageView.setImageURI(currentImageUri);

    }
}
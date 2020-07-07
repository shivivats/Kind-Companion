package com.shivivats.kindcompanion;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

public class NoteAudioView extends AppCompatActivity implements View.OnClickListener {

    private long currentAudioId;

    private Uri currentAudioUri;

    private ImageView playPauseImage;
    private SeekBar seekBar;
    private LinearLayout playLayout;
    private Chronometer chronometer;

    private Toolbar noteAudioViewTopBar;

    private MediaPlayer player = null;

    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_audio_view);

        playPauseImage = findViewById(R.id.audioViewImagePlayPause);
        seekBar = findViewById(R.id.audioViewSeekBar);
        playLayout = findViewById(R.id.audioViewPlayLayout);
        chronometer = findViewById(R.id.audioViewChronometer);

        noteAudioViewTopBar = findViewById(R.id.audioViewTopBar);

        setSupportActionBar(noteAudioViewTopBar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        ab.setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        currentAudioId = intent.getLongExtra("AUDIO_ID", -1);

        if (currentAudioId == -1) {
            setResult(-2);
            finish();
        }

        currentAudioUri = Uri.parse(intent.getStringExtra("AUDIO_URI"));

        playPauseImage.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == playPauseImage) {
            if (!isPlaying) {
                isPlaying = true;
                startPlaying();
            } else {
                isPlaying = false;
                stopPlaying();
            }
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(getApplicationContext(), currentAudioUri);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("AUDIO_VIEW", "prepare() failed");
        }

        playPauseImage.setImageResource(R.drawable.ic_pause);

        if (seekBar.getMax() != 0 && seekBar.getProgress() == seekBar.getMax()) {
            lastProgress = 0;
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        seekBar.setProgress(lastProgress);
        player.seekTo(lastProgress);
        seekBar.setMax(player.getDuration());
        seekUpdation();
        chronometer.start();


        /** once the audio is complete, timer is stopped here **/
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPauseImage.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometer.stop();
                //chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });

        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    //here the track's progress is being changed as per the progress bar
                    player.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometer.setBase(SystemClock.elapsedRealtime() - player.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }

        //showing the play button
        playPauseImage.setImageResource(R.drawable.ic_play);
        chronometer.stop();


        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    //here the track's progress is being changed as per the progress bar
                    player.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometer.setBase(SystemClock.elapsedRealtime() - player.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void seekUpdation() {
        if (player != null) {
            int mCurrentPosition = player.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_note_audio_view, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_note_audio:
                DeleteCurrentAudio();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void DeleteCurrentAudio() {
        Intent intent = new Intent();
        intent.putExtra("AUDIO_ID", currentAudioId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
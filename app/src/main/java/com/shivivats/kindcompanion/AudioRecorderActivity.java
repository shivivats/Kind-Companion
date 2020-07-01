package com.shivivats.kindcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRecorderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "AudioRecorder";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private Toolbar audioRecorderTopBar;
    private Chronometer chronometer;
    private ImageView recordImage;
    private ImageView stopImage;
    private ImageView playPauseImage;
    private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder;
    private LinearLayout linearLayoutPlayer;

    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) {
            finish();
        }

    }

    private void initViews() {
        audioRecorderTopBar = findViewById(R.id.audioRecorderTopBar);

        setSupportActionBar(audioRecorderTopBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        ab.setDisplayShowTitleEnabled(false);

        linearLayoutRecorder = findViewById(R.id.audioRecorderLinearLayout);
        linearLayoutPlayer = findViewById(R.id.audioRecorderPlayLayout);
        chronometer = findViewById(R.id.audioRecorderChronometer);
        recordImage = findViewById(R.id.audioRecorderImageRecord);
        stopImage = findViewById(R.id.audioRecorderImageStop);
        playPauseImage = findViewById(R.id.audioRecorderImagePlayPause);
        seekBar = findViewById(R.id.audioRecorderPlaySeekBar);

        recordImage.setOnClickListener(this);
        stopImage.setOnClickListener(this);
        playPauseImage.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_audio_record, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_record:
                // save recording here
                Uri audioUri = Uri.fromFile(new File(fileName));

                Intent intent = new Intent();
                intent.putExtra("AUDIO_URI", audioUri.toString());
                setResult(RESULT_OK, intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == recordImage) {
            prepareforRecording();
            startRecording();
        } else if (v == stopImage) {
            prepareforStop();
            stopRecording();
        } else if (v == playPauseImage) {
            if (!isPlaying && fileName != null) {
                isPlaying = true;
                startPlaying();
            } else {
                isPlaying = false;
                stopPlaying();
            }
        }
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        playPauseImage.setImageResource(R.drawable.ic_pause);

        if(seekBar.getMax()!=0 && seekBar.getProgress()==seekBar.getMax()) {
            lastProgress=0;
            chronometer.setBase(SystemClock.elapsedRealtime());

        }

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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if (player != null) {
            int mCurrentPosition = player.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
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

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        try {
            fileName = CreateAudioFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, fileName);

        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();

        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        // starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        // show play button
        playPauseImage.setImageResource(R.drawable.ic_play);
        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
    }

    private void prepareforRecording() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        recordImage.setVisibility(View.GONE);
        stopImage.setVisibility(View.VISIBLE);
        linearLayoutPlayer.setVisibility(View.GONE);
    }

    private void prepareforStop() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        recordImage.setVisibility(View.VISIBLE);
        stopImage.setVisibility(View.GONE);
        linearLayoutPlayer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    private String CreateAudioFile() throws IOException {
        // Create an audio file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = "AUDIO_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
        File audio = File.createTempFile(
                audioFileName,  /* prefix */
                ".3gp",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file path for use with ACTION_VIEW intents
        String currentAudioPath = audio.getAbsolutePath();
        return currentAudioPath;
    }

}
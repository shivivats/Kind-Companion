package com.shivivats.kindcompanion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.transition.TransitionManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRecorderActivity extends AppCompatActivity implements View.OnClickListener, RecordAudioDialogFragment.RecordAudioListener {

    private static final String LOG_TAG = "AudioRecorder";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private Chronometer chronometer;
    private ImageView recordImage;
    private ImageView stopImage;
    private ImageView playPauseImage;
    private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder;
    private LinearLayout linearLayoutPlayer;
    private MediaRecorder recorder;
    private MediaPlayer player;

    private int lastProgress = 0;
    private final Handler mHandler = new Handler();
    final Runnable runnable = this::seekUpdate;
    private boolean isPlaying = false;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_audio_recorder);

        fileName = null;
        recorder = null;
        player = null;
        isPlaying = false;
        isRecording = false;


        initViews();
        if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_DENIED) {
            DialogFragment newFragment = new RecordAudioDialogFragment();
            newFragment.show(getSupportFragmentManager(), "record_audio_permission");
        }
    }

    private void initViews() {
        Toolbar audioRecorderTopBar = findViewById(R.id.audioRecorderTopBar);

        setSupportActionBar(audioRecorderTopBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topBar
        if (ab != null)
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) {
            //Snackbar.make(recordImage, "Permission not accepted.", Snackbar.LENGTH_SHORT).show();
            setResult(-2);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_audio_record, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (fileName != null && !isRecording) {
            SaveRecording();
        } else {
            finish();
            //NavUtils.navigateUpTo(this, new Intent(this, NoteEditActivity.class));
        }
    }

    private void SaveRecording() {
        //Log.d("RECORDER_TAG", "not recording and not null file");
        //Log.d("RECORDER_TAG", "filename is " + fileName);
        Uri audioUri = Uri.fromFile(new File(fileName));

        Intent intent = new Intent();
        intent.putExtra("AUDIO_URI", audioUri.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_record:
                SaveAudio();
                return true;

            case R.id.action_cancel_record:
                ReturnToNoteEdit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SaveAudio() {
        // save recording here
        //Log.d("RECORDER_TAG", "save pressed");
        if (fileName != null && !isRecording) {
            SaveRecording();

        } else if (isRecording) {
            Snackbar.make(linearLayoutRecorder, "Stop recording first!", Snackbar.LENGTH_SHORT)
                    .show();

        } else {
            Snackbar.make(linearLayoutRecorder, "Nothing to save.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == recordImage) {
            prepareForRecording();
            startRecording();
        } else if (v == stopImage) {
            prepareForStop();
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

    private void prepareForRecording() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        recordImage.setVisibility(View.GONE);
        stopImage.setVisibility(View.VISIBLE);
        linearLayoutPlayer.setVisibility(View.GONE);
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

        //Log.d(LOG_TAG, fileName);

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
        isRecording = true;
    }

    private void prepareForStop() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        recordImage.setVisibility(View.VISIBLE);
        stopImage.setVisibility(View.GONE);
        linearLayoutPlayer.setVisibility(View.VISIBLE);
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        // starting the chronometer
        chronometer.stop();
        isRecording = false;
        chronometer.setBase(SystemClock.elapsedRealtime());
        // show play button
        playPauseImage.setImageResource(R.drawable.baseline_play_arrow_24);

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

        playPauseImage.setImageResource(R.drawable.baseline_pause_24);

        if (seekBar.getMax() != 0 && seekBar.getProgress() == seekBar.getMax()) {
            lastProgress = 0;
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        seekBar.setProgress(lastProgress);
        player.seekTo(lastProgress);
        seekBar.setMax(player.getDuration());
        seekUpdate();
        chronometer.start();


        // once the audio is complete, timer is stopped here
        player.setOnCompletionListener(mp -> {
            playPauseImage.setImageResource(R.drawable.baseline_play_arrow_24);
            isPlaying = false;
            chronometer.stop();
            //chronometer.setBase(SystemClock.elapsedRealtime());
        });

        // moving the track as per the seekBar's position
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    //here the track's progress is being changed as per the progress bar
                    player.seekTo(progress);
                    //timer is being updated as per the progress of the seekBar
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
        playPauseImage.setImageResource(R.drawable.baseline_play_arrow_24);
        chronometer.stop();


        // moving the track as per the seekBar's position
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    //here the track's progress is being changed as per the progress bar
                    player.seekTo(progress);
                    //timer is being updated as per the progress of the seekBar
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

    private void seekUpdate() {
        if (player != null) {
            int mCurrentPosition = player.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    private String CreateAudioFile() throws IOException {
        // Create an audio file name
        // This gives implied locale warning, however this is just for filenames and user wont see it, so that's ok
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = "AUDIO_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
        File audio = File.createTempFile(
                audioFileName,  /* prefix */
                ".3gp",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file path for use with ACTION_VIEW intents
        return audio.getAbsolutePath();
    }

    private void ReturnToNoteEdit() {
        setResult(-3);
        finish();
    }

    @Override
    public void onRecordAudioPositiveClick(DialogFragment dialog) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onRecordAudioNegativeClick(DialogFragment dialog) {
        //Snackbar.make(recordImage, "Consent not given.", Snackbar.LENGTH_SHORT).show();
        setResult(-2);
        finish();
    }
}
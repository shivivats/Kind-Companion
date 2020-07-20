package com.shivivats.kindcompanion;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class ControlledBreathingActivity extends AppCompatActivity {

    CountDownTimer breatheInTimer;
    CountDownTimer breatheOutTimer;

    CountDownTimer startingTimer;

    TextView breathingCounterTextView;
    TextView breathingInfoTextView;
    TextView breathingIndicatorTextView;

    Button breathingStartStopButton;

    MaterialToolbar topBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_controlled_breathing);

        breathingCounterTextView = findViewById(R.id.breathingCounterTextView);
        breathingInfoTextView = findViewById(R.id.breathingInfoTextView);
        breathingStartStopButton = findViewById(R.id.breathingStartStopButton);
        breathingIndicatorTextView = findViewById(R.id.breathingIndicatorTextView);

        breathingIndicatorTextView.setVisibility(View.INVISIBLE);
        breathingCounterTextView.setVisibility(View.INVISIBLE);

        breathingStartStopButton.setText(R.string.breathingStart);
        breathingStartStopButton.setOnClickListener(this::StartBreathing);

        topBar = findViewById(R.id.controlledBreathingTopBar);
        setSupportActionBar(topBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Controlled Breathing");
        }

        // breathe in for 4 seconds
        breatheInTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                breathingCounterTextView.setText(String.valueOf((millisUntilFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                breathingIndicatorTextView.setText(R.string.breatheOut);
                breatheOutTimer.start();
            }
        };

        // breathe out for 7 seconds
        breatheOutTimer = new CountDownTimer(7000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                breathingCounterTextView.setText(String.valueOf((millisUntilFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                breathingIndicatorTextView.setText(R.string.breatheIn);
                breatheInTimer.start();
            }
        };

        startingTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                breathingCounterTextView.setText("Starting in " + ((millisUntilFinished / 1000) + 1) + "...");
            }

            @Override
            public void onFinish() {
                startTimer();
            }
        };
    }

    public void startTimer() {
        breathingIndicatorTextView.setText(R.string.breatheIn);
        //breathingIndicatorTextView.setVisibility(View.VISIBLE);
        //breathingCounterTextView.setVisibility(View.VISIBLE);
        breatheInTimer.start();
        //breathingStartStopButton.setText("Stop");
        //breathingStartStopButton.setOnClickListener(this::StopBreathing);
    }

    public void StartBreathing(View view) {
        breathingIndicatorTextView.setText(R.string.breathingGetReady);
        breathingIndicatorTextView.setVisibility(View.VISIBLE);
        breathingCounterTextView.setVisibility(View.VISIBLE);
        startingTimer.start();
        breathingStartStopButton.setText(R.string.breathingStop);
        breathingStartStopButton.setOnClickListener(this::StopBreathing);
    }

    public void StopBreathing(View view) {
        breatheInTimer.cancel();
        breatheOutTimer.cancel();
        startingTimer.cancel();
        breathingStartStopButton.setText(R.string.breathingStart);
        breathingStartStopButton.setOnClickListener(this::StartBreathing);
        breathingIndicatorTextView.setVisibility(View.INVISIBLE);
        breathingCounterTextView.setVisibility(View.INVISIBLE);
    }
}
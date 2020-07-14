package com.shivivats.kindcompanion;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ControlledBreathingActivity extends AppCompatActivity {

    CountDownTimer breatheInTimer;
    CountDownTimer breatheOutTimer;

    CountDownTimer startingTimer;

    TextView breathingCounterTextView;
    TextView breathingInfoTextView;
    TextView breathingIndicatorTextView;

    Button breathingStartStopButton;

    Toolbar topbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlled_breathing);

        breathingCounterTextView = findViewById(R.id.breathingCounterTextView);
        breathingInfoTextView = findViewById(R.id.breathingInfoTextView);
        breathingStartStopButton = findViewById(R.id.breathingStartStopButton);
        breathingIndicatorTextView = findViewById(R.id.breathingIndicatorTextView);

        breathingIndicatorTextView.setVisibility(View.INVISIBLE);
        breathingCounterTextView.setVisibility(View.INVISIBLE);

        breathingStartStopButton.setText("Start");
        breathingStartStopButton.setOnClickListener(this::StartBreathing);

        topbar = findViewById(R.id.controlledBreathingTopBar);
        setSupportActionBar(topbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Controlled Breathing");


        // breathe in for 4 seconds
        breatheInTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                breathingCounterTextView.setText(String.valueOf((millisUntilFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                breathingIndicatorTextView.setText("Breathe Out.");
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
                breathingIndicatorTextView.setText("Breathe In.");
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
        breathingIndicatorTextView.setText("Breathe In.");
        //breathingIndicatorTextView.setVisibility(View.VISIBLE);
        //breathingCounterTextView.setVisibility(View.VISIBLE);
        breatheInTimer.start();
        //breathingStartStopButton.setText("Stop");
        //breathingStartStopButton.setOnClickListener(this::StopBreathing);
    }

    public void StartBreathing(View view) {
        breathingIndicatorTextView.setText("Get Ready.");
        breathingIndicatorTextView.setVisibility(View.VISIBLE);
        breathingCounterTextView.setVisibility(View.VISIBLE);
        startingTimer.start();
        breathingStartStopButton.setText("Stop");
        breathingStartStopButton.setOnClickListener(this::StopBreathing);
    }

    public void StopBreathing(View view) {
        breatheInTimer.cancel();
        breatheOutTimer.cancel();
        startingTimer.cancel();
        breathingStartStopButton.setText("Start");
        breathingStartStopButton.setOnClickListener(this::StartBreathing);
        breathingIndicatorTextView.setVisibility(View.INVISIBLE);
        breathingCounterTextView.setVisibility(View.INVISIBLE);
    }
}
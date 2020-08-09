package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CalmDownQuicklyActivity extends AppCompatActivity {

    Toolbar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calm_down_quickly);


        topBar = findViewById(R.id.calmDownQuicklyTopBar);
        setSupportActionBar(topBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Calm Down Quickly");
        }
    }

    public void OnClickFiveThingsActivity(View view) {
        Intent dealIntent = new Intent(this, FiveThingsActivity.class);
        startActivity(dealIntent);
    }

    public void OnClickCrossItOffActivity(View view) {
        Intent dealIntent = new Intent(this, CrossItOffActivity.class);
        startActivity(dealIntent);
    }

    public void OnClickControlledBreathingActivity(View view) {
        Intent dealIntent = new Intent(this, ControlledBreathingActivity.class);
        startActivity(dealIntent);
    }

    public void OnClickRelaxingPaintingActivity(View view) {
        Intent dealIntent = new Intent(this, RelaxingPaintingFrontActivity.class);
        startActivity(dealIntent);
    }

    public void OnClickOffMyChestActivity(View view) {
        Intent dealIntent = new Intent(this, OffMyChestActivity.class);
        startActivity(dealIntent);
    }

}
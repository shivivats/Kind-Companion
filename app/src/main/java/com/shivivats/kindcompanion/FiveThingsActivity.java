package com.shivivats.kindcompanion;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class FiveThingsActivity extends AppCompatActivity {

    Toolbar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_things);

        topbar = findViewById(R.id.fiveThingsTopBar);
        setSupportActionBar(topbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Five Things Game");

    }
}
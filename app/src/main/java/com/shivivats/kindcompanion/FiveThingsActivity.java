package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class FiveThingsActivity extends AppCompatActivity {

    Toolbar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_five_things);

        topBar = findViewById(R.id.fiveThingsTopBar);
        setSupportActionBar(topBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Five Things Game");
        }

    }

    public void OnClickProceedButton(View view) {
        Intent fiveThingsIntent = new Intent(this, FiveThingsGameActivity.class);
        startActivity(fiveThingsIntent);
    }
}
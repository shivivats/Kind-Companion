package com.shivivats.kindcompanion;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AnxietyChecklistActivity extends AppCompatActivity {

    // Not adding this right now as I can not find any particular source to quote the youtuber Zer0Doxy on

    Toolbar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anxiety_checklist);

        topBar = findViewById(R.id.anxietyChecklistTopBar);
        setSupportActionBar(topBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Anxiety Checklist");
        }

    }
}
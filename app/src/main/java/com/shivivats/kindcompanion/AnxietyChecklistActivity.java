package com.shivivats.kindcompanion;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AnxietyChecklistActivity extends AppCompatActivity {

    // Not adding this right now as I can not find any particular source to quote the youtuber Zer0Doxy on

    Toolbar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anxiety_checklist);

        topbar = findViewById(R.id.anxietyChecklistTopbar);
        setSupportActionBar(topbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Anxiety Checklist");
    }
}
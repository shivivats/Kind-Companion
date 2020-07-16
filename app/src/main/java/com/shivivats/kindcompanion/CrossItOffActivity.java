package com.shivivats.kindcompanion;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CrossItOffActivity extends AppCompatActivity {

    //Future considerations: implementing an actual checklist in this activity

    Toolbar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cross_it_off);

        topbar = findViewById(R.id.crossItOffTopbar);
        setSupportActionBar(topbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Cross It Off");
    }
}
package com.shivivats.kindcompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RelaxingPaintingFrontActivity extends AppCompatActivity {

    Toolbar topbar;

    TextView relaxingPaintingHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxing_painting_front);

        topbar = findViewById(R.id.relaxingPaintingFrontTopBar);
        setSupportActionBar(topbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Relaxing Painting");

        relaxingPaintingHeaderView = findViewById(R.id.relaxingPaintingHeaderView);
    }

    public void OnStartPaintingClick(View view) {
        Intent intent = new Intent(this, RelaxingPaintingActivity.class);
        startActivity(intent);
    }

    public void OnRelaxingMusicClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=relaxing+music"));
        startActivity(intent);
    }


}
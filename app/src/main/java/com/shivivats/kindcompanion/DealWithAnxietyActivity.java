package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DealWithAnxietyActivity extends AppCompatActivity {

    // here we have the 5 things game
    // maybe a drawing/colouring game as well
    // implementing breathing techniques

    Toolbar topBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_deal_with_anxiety);

        topBar = findViewById(R.id.dealWithAnxietyTopBar);
        setSupportActionBar(topBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Deal With Anxiety");
        }
    }


    public void FiveThingsGame(View view) {
        Intent intent = new Intent(this, FiveThingsActivity.class);
        startActivity(intent);
    }

    public void AnxietyChecklist(View view) {
        Intent intent = new Intent(this, AnxietyChecklistActivity.class);
        startActivity(intent);
    }

    public void CrossItOff(View view) {
        Intent intent = new Intent(this, CrossItOffActivity.class);
        startActivity(intent);
    }

    public void ControlledBreathing(View view) {
        Intent intent = new Intent(this, ControlledBreathingActivity.class);
        startActivity(intent);
    }

    public void PaintingGame(View view) {
        Intent intent = new Intent(this, RelaxingPaintingFrontActivity.class);
        startActivity(intent);
    }
/*
    public void RelaxingMusic(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=relaxing+music"));
        startActivity(intent);
    }
    */

}
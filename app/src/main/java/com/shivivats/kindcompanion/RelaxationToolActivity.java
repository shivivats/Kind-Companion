package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

public class RelaxationToolActivity extends AppCompatActivity {

    Toolbar toolbar;

    // actually implement a toolbox in the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_relaxation_tool);

        toolbar = findViewById(R.id.relaxationToolTopBar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Relaxation");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //Toast.makeText(getApplicationContext(),"up button pressed", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent upIntent = new Intent(this, UsefulInformationActivity.class);
        upIntent.putExtra("TAB_NAME", "TOOLS");
        NavUtils.navigateUpTo(this, upIntent);
    }
}
package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.material.appbar.MaterialToolbar;

public class AnxietyInfoActivity extends AppCompatActivity {

    //Toolbar toolbar;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_anxiety_info);

        toolbar = findViewById(R.id.anxietyInfoTopBar);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Anxiety Information");
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
        upIntent.putExtra("TAB_NAME", "INFO");
        NavUtils.navigateUpTo(this, upIntent);
    }
}
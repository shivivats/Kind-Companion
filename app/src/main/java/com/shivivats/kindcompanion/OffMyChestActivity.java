package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OffMyChestActivity extends AppCompatActivity {

    Toolbar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_off_my_chest);

        topBar = findViewById(R.id.offMyChestTopBar);
        setSupportActionBar(topBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Get It Off My Chest");
        }

    }

    public void onClickReminderButton(View v) {
        Intent intent = new Intent(this, ReminderNoteListActivity.class);
        startActivity(intent);
    }

    public void onClickVaultButton(View v) {
        Intent intent = new Intent(this, VaultLoginActivity.class);
        startActivity(intent);
    }
}
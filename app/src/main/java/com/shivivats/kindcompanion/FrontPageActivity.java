package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class FrontPageActivity extends AppCompatActivity {

    SeekBar textScaleSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
    }

    public void OnClickVault(View view) {
        Intent vaultIntent = new Intent(this, VaultLogin.class);
        startActivity(vaultIntent);
    }

    public void OnClickReminders(View view) {
        Intent reminderIntent = new Intent(this, ReminderNoteListActivity.class);
        startActivity(reminderIntent);
    }

    public void OnClickSettingsButton(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    public void OnClickUsefulResources(View view) {
        Intent resourcesIntent = new Intent(this, UsefulKnowledgeActivity.class);
        startActivity(resourcesIntent);
    }

    public void OnClickDealWithAnxiety(View view) {
        Intent dealIntent = new Intent(this, DealWithAnxietyActivity.class);
        startActivity(dealIntent);
    }

    public void OffMyChestActivity(View view) {

    }
}


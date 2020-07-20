package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class FrontPageActivity extends AppCompatActivity {

    SeekBar textScaleSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_front_page);
    }

    public void OnClickVault(View view) {
        Intent vaultIntent = new Intent(this, VaultLoginActivity.class);
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

    public void OnClickUsefulInformation(View view) {
        Intent resourcesIntent = new Intent(this, UsefulInformationActivity.class);
        startActivity(resourcesIntent);
    }

    public void OnClickDealWithAnxiety(View view) {
        Intent dealIntent = new Intent(this, DealWithAnxietyActivity.class);
        startActivity(dealIntent);
    }

    public void OnClickOffMyChest(View view) {
        Intent dealIntent = new Intent(this, OffMyChestActivity.class);
        startActivity(dealIntent);
    }
}


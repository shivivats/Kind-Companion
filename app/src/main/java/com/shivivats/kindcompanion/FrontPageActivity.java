package com.shivivats.kindcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class FrontPageActivity extends AppCompatActivity {

    SeekBar textScaleSeekBar;
    TextView frontPageHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_front_page);
        frontPageHeader = findViewById(R.id.frontPageHeader);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("username", "");

        if (frontPageHeader != null && !TextUtils.equals(username, "")) {
            frontPageHeader.setText("Welcome to Kind Companion, " + username + "!");
        }
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


package com.shivivats.kindcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Random;

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

        if (frontPageHeader != null) {
            TypedArray phrases = getResources().obtainTypedArray(R.array.welcome_phrases);
            Random rand = new Random();
            int randomInt = rand.nextInt(phrases.length());
            String randomString = phrases.getString(randomInt);

            /*
            if(randomInt == 1)
            {

                randomString = randomString.concat(", " + username);
            }
             */

            frontPageHeader.setText(randomString);
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

    public void OnClickCalmDownQuickly(View view) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String calmDownMethod = sharedPreferences.getString("calmDownMethod", "no_preference");
        Intent calmDownIntent;
        switch (calmDownMethod) {
            case "five_things":
                calmDownIntent = new Intent(this, FiveThingsActivity.class);
                break;
            case "cross_it_off":
                calmDownIntent = new Intent(this, CrossItOffActivity.class);
                break;
            case "controlled_breathing":
                calmDownIntent = new Intent(this, ControlledBreathingActivity.class);
                break;
            case "relaxing_painting":
                calmDownIntent = new Intent(this, RelaxingPaintingFrontActivity.class);
                break;
            case "off_my_chest":
                calmDownIntent = new Intent(this, OffMyChestActivity.class);
                break;
            case "no_preference":
                calmDownIntent = new Intent(this, OffMyChestActivity.class);
                break;
            default:
                calmDownIntent = new Intent(this, OffMyChestActivity.class);
                break;
        }
        startActivity(calmDownIntent);
    }
}


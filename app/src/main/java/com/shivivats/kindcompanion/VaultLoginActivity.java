package com.shivivats.kindcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class VaultLoginActivity extends AppCompatActivity {

    Button loginButton;
    TextView messageText;
    EditText pinEntry;

    Toolbar topBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_vault_login);

        loginButton = findViewById(R.id.vaultLoginButton);
        messageText = findViewById(R.id.vaultMessageText);
        pinEntry = findViewById(R.id.vaultPinEntry);

        topBar = findViewById(R.id.vaultLoginTopBar);
        setSupportActionBar(topBar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Vault Login");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        messageText.setVisibility(View.INVISIBLE);
    }

    public void OnLoginClick(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Log.d("VAULT_TAG", "pin is " + sharedPreferences.getString("vaultPin", "1234") + ", entered pin is " + pinEntry.getText());
        if (pinEntry.getText().toString().equals(sharedPreferences.getString("vaultPin", "1234"))) {

            Intent intent = new Intent(this, VaultNoteListActivity.class);
            startActivity(intent);
        } else {
            messageText.setVisibility(View.VISIBLE);
            messageText.setText(R.string.vaultLoginWrongPin);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_vaults, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_vault_pin:
                OpenSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void OpenSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
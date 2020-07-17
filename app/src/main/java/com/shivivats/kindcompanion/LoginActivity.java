package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //createNotificationChannel();

        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                if (getIntent().getBooleanExtra("FROM_NOTIFICATION", false)) {
                    // we got here from the notification
                    LoadReminderNoteList();
                } else {
                    LoadFrontPage();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                //.setNegativeButtonText("Use account password")
                .setConfirmationRequired(false)
                .setDeviceCredentialAllowed(true)
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        Button biometricLoginButton = findViewById(R.id.biometricLoginButton);
        biometricLoginButton.setOnClickListener(view -> {
            if (PreferenceManager.getDefaultSharedPreferences(this /* Activity context */).getBoolean("biometrics", true) == true) {
                biometricPrompt.authenticate(promptInfo);
            } else {
                LoadFrontPage();
            }
        });
    }

    private void LoadFrontPage() {
        Intent intent = new Intent(LoginActivity.this, FrontPageActivity.class);
        startActivity(intent);
    }

    private void LoadReminderNoteList() {
        Intent intent = new Intent(LoginActivity.this, ReminderNoteListActivity.class);
        startActivity(intent);
    }


}
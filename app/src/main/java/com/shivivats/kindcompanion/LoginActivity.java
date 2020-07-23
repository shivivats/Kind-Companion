package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    Button appLoginButton;
    EditText appLoginPinEntry;
    TextView appLoginPinMessageText;
    Button appLoginPinButton;


    boolean biometricAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_login);

        appLoginButton = findViewById(R.id.appLoginButton);
        appLoginPinEntry = findViewById(R.id.appLoginPinEntry);
        appLoginPinMessageText = findViewById(R.id.appLoginPinMessageText);
        appLoginPinButton = findViewById(R.id.appLoginPinButton);

        //createNotificationChannel();

        biometricAvailable = false;

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                biometricAvailable = true;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                biometricAvailable = false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                biometricAvailable = false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                biometricAvailable = false;
                break;
        }

        if (biometricAvailable) {
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
                    LogIntoApp();
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
                    .setTitle("Biometric login for Kind Companion")
                    .setSubtitle("Log in using your biometric credential")
                    //.setNegativeButtonText("Use account password")
                    .setConfirmationRequired(false)
                    .setDeviceCredentialAllowed(true)
                    .build();
        }

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        Button appLoginButton = findViewById(R.id.appLoginButton);
        appLoginButton.setOnClickListener(view -> {
            if (PreferenceManager.getDefaultSharedPreferences(this /* Activity context */).getBoolean("biometrics", false) && biometricAvailable) {
                biometricPrompt.authenticate(promptInfo);
            } else if (PreferenceManager.getDefaultSharedPreferences(this /* Activity context */).getBoolean("useLoginPin", false)) {
                appLoginButton.setVisibility(View.INVISIBLE);
                appLoginPinEntry.setVisibility(View.VISIBLE);
                appLoginPinMessageText.setVisibility(View.INVISIBLE);
                appLoginPinButton.setVisibility(View.VISIBLE);
            } else {
                LogIntoApp();
            }
        });
    }

    private void LogIntoApp() {
        if (getIntent().getBooleanExtra("FROM_NOTIFICATION", false)) {
            // we got here from the notification
            LoadReminderNoteList();
        } else {
            LoadFrontPage();
        }
    }

    private void LoadFrontPage() {
        Intent intent = new Intent(LoginActivity.this, FrontPageActivity.class);
        startActivity(intent);
    }

    private void LoadReminderNoteList() {
        Intent intent = new Intent(LoginActivity.this, ReminderNoteListActivity.class);
        startActivity(intent);
    }


    public void checkPinCorrect(View view) {
        String currentAppPin = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */).getString("appPin", "1234");

        if (appLoginPinEntry != null && TextUtils.equals(appLoginPinEntry.getText(), currentAppPin)) {
            LogIntoApp();
        } else {
            appLoginPinMessageText.setVisibility(View.VISIBLE);
        }
    }

}
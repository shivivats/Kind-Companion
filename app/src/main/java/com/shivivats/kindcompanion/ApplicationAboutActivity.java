package com.shivivats.kindcompanion;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class ApplicationAboutActivity extends AppCompatActivity {

    //Toolbar toolbar;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_application_about);


        toolbar = findViewById(R.id.applicationAboutTopBar);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("About");
        }

        TextView applicationInfoTextFour = findViewById(R.id.applicationInfoTextFour);
        applicationInfoTextFour.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
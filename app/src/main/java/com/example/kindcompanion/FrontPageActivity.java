package com.example.kindcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class FrontPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
    }

    public void OnClickNewNote(View view) {
        Intent newNoteIntent = new Intent(this, NoteActivity.class);
        Log.d("FrontPageActivity", "New Note Button Intent");
        startActivity(newNoteIntent);
    }
}

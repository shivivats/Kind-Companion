package com.shivivats.kindcompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NoteImageView extends AppCompatActivity {

    private long currentImageId;

    private Uri currentImageUri;

    private ImageView imageView;

    private Toolbar noteImageViewTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_image_view);

        imageView = findViewById(R.id.noteImageImageView);

        noteImageViewTopBar = findViewById(R.id.noteImageTopBar);

        setSupportActionBar(noteImageViewTopBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        ab.setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        currentImageId = intent.getLongExtra("IMAGE_ID", -1);

        if (currentImageId == -1) {
            setResult(-2);
            finish();
        }

        currentImageUri = Uri.parse(intent.getStringExtra("IMAGE_URI"));

        imageView.setImageURI(currentImageUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_note_image_view, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_note_image:
                DeleteCurrentImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void DeleteCurrentImage() {
        Intent intent = new Intent();
        intent.putExtra("IMAGE_ID", currentImageId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
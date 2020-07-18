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
import androidx.fragment.app.DialogFragment;

public class NoteImageView extends AppCompatActivity implements DeleteImageDialogFragment.DeleteImageListener {

    private long currentImageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_note_image_view);

        ImageView imageView = findViewById(R.id.noteImageImageView);

        Toolbar noteImageViewTopBar = findViewById(R.id.noteImageTopBar);

        setSupportActionBar(noteImageViewTopBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topBar
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        currentImageId = intent.getLongExtra("IMAGE_ID", -1);

        if (currentImageId == -1) {
            setResult(-2);
            finish();
        }

        Uri currentImageUri = Uri.parse(intent.getStringExtra("IMAGE_URI"));

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
                //show a dialog here that asks the user if they're sure
                DialogFragment newFragment = new DeleteImageDialogFragment();
                newFragment.show(getSupportFragmentManager(), "delete_image");
                return true;

            case R.id.action_cancel_note_image:
                ReturnToNoteEdit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void ReturnToNoteEdit() {
        setResult(-5);
        finish();
    }

    private void DeleteCurrentImage() {
        Intent intent = new Intent();
        intent.putExtra("IMAGE_ID", currentImageId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDeleteImageDialogPositiveClick(DialogFragment dialog) {
        DeleteCurrentImage();
    }

    @Override
    public void onDeleteImageDialogNegativeClick(DialogFragment dialog) {

    }
}
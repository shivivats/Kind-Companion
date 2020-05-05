package com.shivivats.kindcompanion;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderNoteEntryActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.shivivats.kindcompanion.";

    EditText noteTitle;
    EditText noteBody;

    Toolbar reminderEntryHeaderBar;
    Toolbar reminderEntryBottomBar;

    //static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_note_entry);

        // we basically just set the toolbar as the support action bar so we can get it using a function anywhere in the activity now
        reminderEntryHeaderBar = findViewById(R.id.reminderNoteEntryTopBar);
        setSupportActionBar(reminderEntryHeaderBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        ab.setDisplayShowTitleEnabled(false);

        // set the views
        noteTitle = findViewById(R.id.reminderNoteEntryTitleField);
        noteBody = findViewById(R.id.reminderNoteEntryTextField);

        // set the bottom bar
        reminderEntryBottomBar = findViewById(R.id.reminderNoteEntryBottomBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_reminder_note_entry, menu);

        Menu bottomMenu = reminderEntryBottomBar.getMenu();
        getMenuInflater().inflate(R.menu.bottombar_reminder_note_entry, bottomMenu);

        for (int i=0;i<bottomMenu.size();i++) {
            bottomMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return onOptionsItemSelected(menuItem);
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_reminder_note:
                StoreReminder();
                return true;

            case R.id.action_choose_image:
                ChooseImage();
                return true;

            case R.id.action_draw_image:
                DrawImage();
                return true;

            case R.id.action_take_photo:
                TakePhoto();
                return true;

            case R.id.action_record_audio:
                RecordAudio();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                // We dont need to handle the "up/back" button here bc its gonna default to the superclass
                return super.onOptionsItemSelected(item);
        }
    }

    private void StoreReminder() {
        // here we do the intent stuff
        Intent replyIntent = new Intent();
        String nt = noteTitle.getText().toString();
        String nb = noteBody.getText().toString();
        //NoteEntity noteEntity= new NoteEntity();
        //noteEntity.noteType=NoteType.NOTE_REMINDER.getValue();
        //noteEntity.noteTitle=nt;
        //noteEntity.noteBody=nb;
        replyIntent.putExtra("noteTitle", nt);
        replyIntent.putExtra("noteBody", nb);
        replyIntent.putExtra("noteType", NoteType.NOTE_REMINDER.getValue());
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    // can do imageView.setImageURI(uri)

                }
            });

    private void ChooseImage() {
        getContent.launch("image/*");
    }

    private void DrawImage() {

    }

    // KEEP THIS IN MIND WHILE USING THIS STUFF
    // public abstract ActivityResultLauncher<I> registerForActivityResult (ActivityResultContract<I, O> contract,
    //                ActivityResultCallback<O> callback)
    // LOOK AT THE Is AND Os
    ActivityResultLauncher<Uri> takeImageLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Bitmap>() {
        @Override
        public void onActivityResult(Bitmap result) {
            // do something with the result here
            // we probably add the image as a thumbnail at the bottom of the note
            // make a new image view and then add the image to the view like:
            // imageView.setImageBitmap(result);

        }
    });

    private void TakePhoto() {
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // Device has a camera

            /*
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager())!=null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            */
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = CreateImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                    "com.shivivats.kindcompanion.fileprovider",
                    photoFile);
                takeImageLauncher.launch(photoURI);
            }
        }else {
            // Device doesnt have a camera, show a toast or something
            Toast.makeText(this, "Camera not found on device.", Toast.LENGTH_LONG);
        }
    }

    private void RecordAudio() {

    }

    private File CreateImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}

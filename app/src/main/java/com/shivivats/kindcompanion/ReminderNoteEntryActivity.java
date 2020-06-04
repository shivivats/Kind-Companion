package com.shivivats.kindcompanion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReminderNoteEntryActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.shivivats.kindcompanion.";

    EditText noteTitle;
    EditText noteBody;

    Toolbar reminderEntryHeaderBar;
    Toolbar reminderEntryBottomBar;

    private long currentNoteId;

    private NoteEntryViewModel noteEntryViewModel;

    //static final int REQUEST_IMAGE_CAPTURE = 1;

    // WE ARE INSERTING THE NOTE INTO THE DATABASE RIGHT FUCKIN NOW AS SOON AS ITS CREATED

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_note_entry);

        Intent intent = getIntent();
        currentNoteId = intent.getLongExtra("CURRENT_NOTE_ID", -1);
        if(currentNoteId==-1) {
            setResult(RESULT_CANCELED);
            finish();
        }

        noteEntryViewModel = new NoteEntryViewModel(getApplication(), currentNoteId);

        // now we have the currentnoteid in the variable and we can use it for various insert operations
        SetToolbars();

        InitRecyclerView();
    }

    private void SetToolbars() {
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

    private void InitRecyclerView() {
        // add the recyler view
        RecyclerView imageRecyclerView = findViewById(R.id.noteEntryRecyclerImageView);
        final NoteImagesAdapter adapter = new NoteImagesAdapter(this);
        imageRecyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        imageRecyclerView.setLayoutManager(manager);

        // associate the viewmodel with provider
        noteEntryViewModel = new ViewModelProvider(this, new NoteEntryViewModelFactory(this.getApplication(), currentNoteId)).get(NoteEntryViewModel.class);

        // add an observer for the livedata
        noteEntryViewModel.getCurrentNoteImages().observe(this, new Observer<List<ImageEntity>>() {
            @Override
            public void onChanged(List<ImageEntity> imageEntities) {
                // update the cached copy of image entities in the adapter
                adapter.setImages(imageEntities);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_reminder_note_entry, menu);

        Menu bottomMenu = reminderEntryBottomBar.getMenu();
        getMenuInflater().inflate(R.menu.bottombar_reminder_note_entry, bottomMenu);

        for (int i = 0; i < bottomMenu.size(); i++) {
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

            case R.id.action_discard_reminder_note:
                DiscardNote();
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
        replyIntent.putExtra("noteId", currentNoteId);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    ActivityResultLauncher<String> chooseImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if(uri!=null) {
                        // Handle the returned Uri
                        // add the image to a database
                        ImageEntity newImage = new ImageEntity();
                        newImage.imageUri = uri;
                        newImage.isDrawing = false;
                        newImage.imageNoteId = currentNoteId;
                        noteEntryViewModel.insert(newImage);
                        // this should automatically add the image to the recyclerview as well
                    }else {
                        // the user chose to not choose an image
                        Toast.makeText(getApplicationContext(), "Image chooser closed.", Toast.LENGTH_LONG);
                    }
                }
            });

    private void ChooseImage() {
        chooseImage.launch("image/*");
    }

    private void DrawImage() {
        Intent drawImageIntent = new Intent(this, PaintActivity.class);
        startActivity(drawImageIntent);
    }

    // KEEP THIS IN MIND WHILE USING THIS STUFF
    // public abstract ActivityResultLauncher<I> = registerForActivityResult (ActivityResultContract<I, O> contract,
    //                ActivityResultCallback<O> callback)
    // LOOK AT THE Is AND Os
    ActivityResultLauncher<Uri> takePicLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            // do something with the result here
            // add the taken image to the database
            if(result==true) {
                Log.d("randomtag","non null camera activity result");
                if (currentPhotoURI != null) {
                    ImageEntity newImage = new ImageEntity();
                    newImage.imageUri = currentPhotoURI;
                    newImage.isDrawing = false;
                    newImage.imageNoteId = currentNoteId;
                    noteEntryViewModel.insert(newImage);
                    // this should automatically add the image to the recyclerview as well
                } else {
                    // for some reason the uri was null, i.e., storage couldnt be accessed
                    Toast.makeText(getApplicationContext(), "Photo couldn't be saved.", Toast.LENGTH_LONG);
                }
            }else {
                Log.d("randomtag","null camera activity result");
                // the user chose to not click a picture
                Toast.makeText(getApplicationContext(), "Camera closed.", Toast.LENGTH_LONG);
            }
        }
    });

    Uri currentPhotoURI=null;

    private void TakePhoto() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // Device has a camera

            /*
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager())!=null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            */
            // Create the File where the photo should go
            File photoFile = null;
            currentPhotoURI=null;
            //Log.d("note-entry-camera-stuff","file is null");
            try {
                photoFile = CreateImageFile();
                //Log.d("note-entry-camera-stuff","file is created");
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.shivivats.kindcompanion.fileprovider",
                        photoFile);
                currentPhotoURI = photoURI;
                Log.d("randomtag", photoURI.toString());
                takePicLauncher.launch(photoURI);
                //Log.d("note-entry-camera-stuff","launched the take image launcher");
            }
        } else {
            // Device doesnt have a camera, show a toast or something
            Toast.makeText(this, "Camera not found on device.", Toast.LENGTH_LONG);
        }
    }

    private void RecordAudio() {

    }

    private void DiscardNote() {
        setResult(-2);
        finish();
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

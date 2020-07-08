package com.shivivats.kindcompanion;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteEditActivity extends AppCompatActivity implements NoteEditImagesClickListener, NoteEditAudioClickListener {

    public static final String EXTRA_REPLY = "com.shivivats.kindcompanion.";

    EditText noteTitle;
    EditText noteBody;

    Toolbar noteEditHeaderBar;
    Toolbar noteEditBottomBar;
    Uri currentPhotoURI = null;
    private long currentNoteId;
    private NoteEditViewModel noteEditViewModel;

    ActivityResultLauncher<Intent> openImageEditActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // here, we do -2 for delete
            // -3 for update
            // -4 for something went wrong
            // and else
            if (result.getResultCode() == -2) {
                // get the image id from the image here
                long currentImageID = result.getData().getLongExtra("IMAGE_ID", -1);
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.imageId = currentImageID;
                noteEditViewModel.deleteImages(imageEntity);
                Toast.makeText(getApplicationContext(), "Image deleted.", Toast.LENGTH_LONG).show();
            } else if (result.getResultCode() == -3) {
                long currentImageID = result.getData().getLongExtra("IMAGE_ID", -1);
                Uri uri = Uri.parse(result.getData().getStringExtra("IMAGE_URI"));
                boolean isDrawing = result.getData().getBooleanExtra("IS_DRAWING", true);

                ImageEntity imageEntity = new ImageEntity();
                imageEntity.imageId = currentImageID;
                imageEntity.imageUri = uri;
                imageEntity.isDrawing = isDrawing;
                imageEntity.imageNoteId = currentNoteId;
                noteEditViewModel.updateImages(imageEntity);
                Toast.makeText(getApplicationContext(), "Image updated.", Toast.LENGTH_LONG).show();
            } else if (result.getResultCode() == -4) {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            } else {
                // something unpredicted happened so i guess not delete image, so just leave it as is?
                // this is also used for back button i guess now
                // in other words, do nothing

            }
        }
    });
    ActivityResultLauncher<Intent> openAudioActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                // get the audio id here
                long currentAudioId = result.getData().getLongExtra("AUDIO_ID", -1);
                AudioEntity audioEntity = new AudioEntity();
                audioEntity.audioId = currentAudioId;
                noteEditViewModel.deleteAudio(audioEntity);
                Toast.makeText(getApplicationContext(), "Audio deleted.", Toast.LENGTH_LONG).show();
            } else if (result.getResultCode() == -2) {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            } else {
                // something unpredicted happened so i guess not delete image, so just leave it as is?
                // this is also used for back button i guess now
                // in other words, do nothing

            }
        }
    });
    ActivityResultLauncher<String[]> chooseImage = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri != null) {
                // Handle the returned Uri
                // add the image to a database
                ImageEntity newImage = new ImageEntity();
                newImage.imageUri = uri;
                newImage.isDrawing = false;
                newImage.imageNoteId = currentNoteId;
                noteEditViewModel.insertImages(newImage);
                // this should automatically add the image to the recyclerview as well
            } else {
                // the user chose to not choose an image
                Toast.makeText(getApplicationContext(), "Image chooser closed.", Toast.LENGTH_LONG);
            }
        }
    });
    ActivityResultLauncher<Intent> drawingActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                // the uri will be an extra
                Uri uri = Uri.parse(result.getData().getStringExtra("DRAWING_URI"));

                // add the image to a database
                ImageEntity newImage = new ImageEntity();
                newImage.imageUri = uri;
                newImage.isDrawing = true;
                newImage.imageNoteId = currentNoteId;
                noteEditViewModel.insertImages(newImage);
            } else {
                Toast.makeText(getApplicationContext(), "Drawing could not be saved.", Toast.LENGTH_SHORT).show();
            }
        }
    });
    ActivityResultLauncher<Uri> takePicLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            // do something with the result here
            // add the taken image to the database
            if (result == true) {
                Log.d("randomtag", "non null camera activity result");
                if (currentPhotoURI != null) {
                    ImageEntity newImage = new ImageEntity();
                    newImage.imageUri = currentPhotoURI;
                    newImage.isDrawing = false;
                    newImage.imageNoteId = currentNoteId;
                    noteEditViewModel.insertImages(newImage);
                    // this should automatically add the image to the recyclerview as well
                } else {
                    // for some reason the uri was null, i.e., storage couldnt be accessed
                    Toast.makeText(getApplicationContext(), "Photo couldn't be saved.", Toast.LENGTH_LONG);
                }
            } else {
                Log.d("randomtag", "null camera activity result");
                // the user chose to not click a picture
                Toast.makeText(getApplicationContext(), "Camera closed.", Toast.LENGTH_LONG);
            }
        }
    });
    ActivityResultLauncher<Intent> recordAudioLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                // the uri will be an extra
                Uri uri = Uri.parse(result.getData().getStringExtra("AUDIO_URI"));

                // add the audio to the database
                AudioEntity newAudio = new AudioEntity();
                newAudio.audioUri = uri;
                newAudio.audioNoteId = currentNoteId;
                newAudio.isRecording = true;
                noteEditViewModel.insertAudio(newAudio);
                Toast.makeText(getApplicationContext(), "Recording saved successfully.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Audio could not be saved.", Toast.LENGTH_SHORT).show();
            }
        }
    });

    ActivityResultLauncher<Intent> openImageViewActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                // get the image id from the image here
                long currentImageID = result.getData().getLongExtra("IMAGE_ID", -1);
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.imageId = currentImageID;
                noteEditViewModel.deleteImages(imageEntity);
                Toast.makeText(getApplicationContext(), "Image deleted.", Toast.LENGTH_LONG).show();
            } else if (result.getResultCode() == -2) {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            } else {
                // something unpredicted happened so i guess not delete image, so just leave it as is?
                // this is also used for back button i guess now
                // in other words, do nothing

            }
        }
    });


    private int currentNoteType;
    private NoteEditImagesAdapter noteEditImagesAdapter;
    private NoteEditAudioAdapter noteEditAudioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        Intent intent = getIntent();
        currentNoteId = intent.getLongExtra("CURRENT_NOTE_ID", -1);
        if (currentNoteId == -1) {
            setResult(RESULT_CANCELED);
            finish();
        }
        currentNoteType = intent.getIntExtra("CURRENT_NOTE_TYPE", -1);
        if (currentNoteId == -1) {
            setResult(RESULT_CANCELED);
            finish();
        }

        noteEditViewModel = new NoteEditViewModel(getApplication(), currentNoteId);

        // set the views
        noteTitle = findViewById(R.id.noteEditTitleField);
        noteBody = findViewById(R.id.noteEditBodyField);

        noteTitle.setText(intent.getStringExtra("CURRENT_NOTE_TITLE"));
        noteBody.setText(intent.getStringExtra("CURRENT_NOTE_BODY"));

        // now we have the currentnoteid in the variable and we can use it for various insert operations
        SetToolbars();

        InitRecyclerView();
    }

    private void SetToolbars() {
        // we basically just set the toolbar as the support action bar so we can get it using a function anywhere in the activity now
        noteEditHeaderBar = findViewById(R.id.noteEditTopBar);
        setSupportActionBar(noteEditHeaderBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // hide the title from the topbar
        if (TextUtils.isEmpty(noteTitle.getText())) {
            ab.setDisplayShowTitleEnabled(false);
        } else {
            ab.setTitle(noteTitle.getText().toString());
        }

        // set the bottom bar
        noteEditBottomBar = findViewById(R.id.noteEditBottomBar);
    }

    private void InitRecyclerView() {
        // add the recyler view for the image
        RecyclerView imageRecyclerView = findViewById(R.id.noteEditRecyclerImageView);

        GridLayoutManager imageViewManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        imageRecyclerView.setLayoutManager(imageViewManager);

        noteEditImagesAdapter = new NoteEditImagesAdapter(this);
        noteEditImagesAdapter.setNoteEditImagesClickListener(this);

        imageRecyclerView.setAdapter(noteEditImagesAdapter);

        // so we prolly wanna load all the images sent to us. wait no we dont? - IDK WHY THIS COMMENT EXISTS BUT IM TOO AFRAID TO DELETE IT

        // add recycler view for the audios
        RecyclerView audioRecyclerView = findViewById(R.id.noteEditRecyclerAudioView);

        LinearLayoutManager audioViewManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        audioRecyclerView.setLayoutManager(audioViewManager);

        noteEditAudioAdapter = new NoteEditAudioAdapter(this);
        noteEditAudioAdapter.setNoteEditAudioClickListener(this);

        audioRecyclerView.setAdapter(noteEditAudioAdapter);


        // associate the viewmodel with provider
        noteEditViewModel = new ViewModelProvider(this, new NoteEditViewModelFactory(this.getApplication(), currentNoteId)).get(NoteEditViewModel.class);

        // add an observer for the livedata
        noteEditViewModel.getCurrentNoteImages().observe(this, new Observer<List<ImageEntity>>() {
            @Override
            public void onChanged(List<ImageEntity> imageEntities) {
                // update the cached copy of image entities in the adapter
                noteEditImagesAdapter.setImages(imageEntities);
            }
        });

        noteEditViewModel.getCurrentNoteAudio().observe(this, new Observer<List<AudioEntity>>() {
            @Override
            public void onChanged(List<AudioEntity> audioEntities) {
                // update the cached copy of audio entities in the adapter
                noteEditAudioAdapter.setAudios(audioEntities);
            }
        });
    }

    @Override
    public void onNoteEditImagesClicked(View view, int position) {
        ImageEntity imageEntity = noteEditImagesAdapter.getImagesList().get(position);

        // here we need to open whatever we wanna do with the image.
        // make a new activity to display the image and such?

        if (imageEntity.isDrawing == true) {

            Intent intent = new Intent(NoteEditActivity.this, PaintActivity.class);
            // add intent extras here
            intent.putExtra("IMAGE_ID", imageEntity.imageId);
            intent.putExtra("IMAGE_URI", imageEntity.imageUri.toString());
            intent.putExtra("IS_EDIT", true);

            openImageEditActivity.launch(intent);
        } else {

            // here we need to open whatever we wanna do with the image.
            // make a new activity to display the image and such?


            Intent intent = new Intent(NoteEditActivity.this, NoteImageView.class);
            // add intent extras here
            intent.putExtra("IMAGE_ID", imageEntity.imageId);
            intent.putExtra("IMAGE_URI", imageEntity.imageUri.toString());
            openImageViewActivity.launch(intent);

        }
    }

    @Override
    public void onNoteEditAudioClicked(View view, int position) {
        AudioEntity audioEntity = noteEditAudioAdapter.getAudiosList().get(position);

        // here we do whatever we wanna do with the audio
        // we have the audio file uri so i guess we can just deal with that

        Intent intent = new Intent(NoteEditActivity.this, NoteAudioView.class);

        intent.putExtra("AUDIO_ID", audioEntity.audioId);
        intent.putExtra("AUDIO_URI", audioEntity.audioUri.toString());
        openAudioActivity.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_note_edit, menu);

        Menu bottomMenu = noteEditBottomBar.getMenu();
        getMenuInflater().inflate(R.menu.bottombar_note_edit, bottomMenu);

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
            case R.id.action_save_edit_note:
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

            case R.id.action_delete_edit_note:
                DeleteNote();
                return true;

            case R.id.action_void_edit_note:
                SendToVoid();
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

        // we need to do the same with audio clips at some point
        if (TextUtils.isEmpty(noteTitle.getText()) && TextUtils.isEmpty(noteBody.getText()) && noteEditViewModel.getNumberOfNoteImages() == 0 && noteEditViewModel.getNumberOfNoteAudio() == 0) {
            setResult(-3);
            finish();
        }

        Log.d("randomtagEntryActivity", "note title in entry activity: " + nt);
        Log.d("randomtagEntryActivity", "note body in entry activity: " + nb);

        //NoteEntity noteEntity= new NoteEntity();
        //noteEntity.noteType=NoteType.NOTE_REMINDER.getValue();
        //noteEntity.noteTitle=nt;
        //noteEntity.noteBody=nb;
        replyIntent.putExtra("noteTitle", nt);
        replyIntent.putExtra("noteBody", nb);
        replyIntent.putExtra("noteType", currentNoteType);
        replyIntent.putExtra("noteId", currentNoteId);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    private void ChooseImage() {
        String[] documentTypes = new String[1];
        documentTypes[0] = "image/*";
        chooseImage.launch(documentTypes);
    }

    private void DrawImage() {
        Intent drawImageIntent = new Intent(this, PaintActivity.class);
        drawImageIntent.putExtra("IS_EDIT", false);
        drawingActivity.launch(drawImageIntent);
    }

    private void TakePhoto() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // Device has a camera
            // Create the File where the photo should go
            File photoFile = null;
            currentPhotoURI = null;
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
                currentPhotoURI = photoURI;
                takePicLauncher.launch(photoURI);
            }
        } else {
            // Device doesnt have a camera, show a toast or something
            Toast.makeText(this, "Camera not found on device.", Toast.LENGTH_LONG);
        }
    }

    private void RecordAudio() {
        // ITS OUR TIME TO SHINE
        Intent intent = new Intent(NoteEditActivity.this, AudioRecorderActivity.class);
        recordAudioLauncher.launch(intent);
    }

    private void DeleteNote() {
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

    private void SendToVoid() {
        // we need to create a void animation here
        // and then delete the note
        // so we just
        Toast.makeText(getApplicationContext(), "Your thoughts have been sent into the void, disappearing forever...", Toast.LENGTH_LONG).show();
        DeleteNote();
    }
}

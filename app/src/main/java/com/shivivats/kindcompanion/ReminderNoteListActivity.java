package com.shivivats.kindcompanion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ReminderNoteListActivity extends AppCompatActivity {

    private ReminderNoteViewModel reminderNoteViewModel;
    private long currentNoteId;
    // KEEP THIS IN MIND WHILE USING THIS STUFF
    // public abstract ActivityResultLauncher<I> registerForActivityResult (ActivityResultContract<I, O> contract,
    //                ActivityResultCallback<O> callback)
    // LOOK AT THE Is AND Os
    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // Here we handle the returned data

            if (result.getResultCode() == RESULT_OK) {
                NoteEntity noteEntity = new NoteEntity();
                noteEntity.noteType = result.getData().getIntExtra("noteType", NoteType.NOTE_REMINDER.getValue());
                noteEntity.noteTitle = result.getData().getStringExtra("noteTitle");
                noteEntity.noteBody = result.getData().getStringExtra("noteBody");
                noteEntity.noteId = result.getData().getLongExtra("noteId", -1);

                if (noteEntity.noteId == -1) {
                    DiscardNoteIfExists("The note couldn't be saved.");
                } else if(noteEntity.noteTitle==" " && noteEntity.noteBody==" ") {
                    DiscardNoteIfExists("Empty note discarded.");
                } else {
                    Log.d("randomtag", "note title "+ noteEntity.noteTitle);
                    Log.d("randomtag", "note body "+ noteEntity.noteBody);
                    Toast.makeText(getApplicationContext(), "Note saved successfully.", Toast.LENGTH_LONG).show();
                    reminderNoteViewModel.update(noteEntity);
                }
            } else if (result.getResultCode()==-2) {
                DiscardNoteIfExists("Note discarded.");
            }else {
                DiscardNoteIfExists("The note couldn't be saved.");
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_note_list);

        RecyclerView recyclerView = findViewById(R.id.noteReminderRecyclerView);
        final ReminderNoteListAdapter adapter = new ReminderNoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reminderNoteViewModel = new ViewModelProvider(this).get(ReminderNoteViewModel.class);

        reminderNoteViewModel.getReminderNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable final List<NoteEntity> notes) {
                // update the cached copy of the notes in the adapter
                adapter.setNotes(notes);
            }
        });

        FloatingActionButton fab = findViewById(R.id.reminderListFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAddFabClick();
            }
        });
    }

    private void DiscardNoteIfExists(String toastText) {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.noteId = currentNoteId;
        reminderNoteViewModel.delete(noteEntity);
        Toast.makeText(
                getApplicationContext(),
                toastText,
                Toast.LENGTH_LONG).show();
    }

    public void OnAddFabClick() {
        // we should insert a blank note here
        // and we should send the data of the note id with the intent
        InsertBlankNote();

        // now we send the intent
        Intent intent = new Intent(ReminderNoteListActivity.this, ReminderNoteEntryActivity.class);
        intent.putExtra("CURRENT_NOTE_ID", currentNoteId);
        startActivityForResult.launch(intent);
    }

    private void InsertBlankNote() {
        // basically we create a new blank note here, insert it into the database so we have the note id to refer to the images and voice recordings etc
        NoteEntity blankNote = new NoteEntity();
        blankNote.noteBody = "";
        blankNote.noteTitle = "";
        blankNote.noteType = NoteType.NOTE_REMINDER.getValue();

        long noteId = reminderNoteViewModel.insertBlank(blankNote);
        currentNoteId = noteId;
        // now we have the id and we can pass it with the intent
    }
}

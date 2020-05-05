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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ReminderNoteListActivity extends AppCompatActivity {

    private ReminderNoteViewModel reminderNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_note_list);

        RecyclerView recyclerView = findViewById(R.id.noteReminderRecyclerView);
        final ReminderNoteListAdapter adapter = new ReminderNoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reminderNoteViewModel = new ViewModelProvider(this).get(ReminderNoteViewModel.class);

        reminderNoteViewModel.getReminderNotes().observe(this, new Observer<List<NoteTuple>>() {
            @Override
            public void onChanged(@Nullable final List<NoteTuple> notes) {
                // update the cached copy of the words in the adapter
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
                reminderNoteViewModel.insert(noteEntity);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.not_saved,
                        Toast.LENGTH_LONG).show();
            }
        }
    });

    public void OnAddFabClick() {
        Intent intent = new Intent(ReminderNoteListActivity.this, ReminderNoteEntryActivity.class);

        startActivityForResult.launch(intent);
    }
}

package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VaultNoteListActivity extends AppCompatActivity implements NoteListClickListener {

    private VaultNoteViewModel vaultNoteViewModel;
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
                noteEntity.noteType = result.getData().getIntExtra("noteType", NoteType.NOTE_VAULT.getValue());
                noteEntity.noteTitle = result.getData().getStringExtra("noteTitle");
                noteEntity.noteBody = result.getData().getStringExtra("noteBody");
                noteEntity.noteId = result.getData().getLongExtra("noteId", -1);
                noteEntity.noteImagesCount = result.getData().getIntExtra("noteNumberImages", -1);
                noteEntity.noteAudioCount = result.getData().getIntExtra("noteNumberAudios", -1);

                if (noteEntity.noteId == -1 || noteEntity.noteImagesCount == -1 || noteEntity.noteAudioCount == -1) {
                    DiscardNoteIfExists("The note couldn't be saved.");
                } else {
                    Toast.makeText(getApplicationContext(), "Note saved successfully.", Toast.LENGTH_LONG).show();
                    vaultNoteViewModel.update(noteEntity);
                }
            } else if (result.getResultCode() == -2) {
                DiscardNoteIfExists("Note deleted.");
            } else if (result.getResultCode() == -3) {
                DiscardNoteIfExists("Empty note discarded.");
            } else {
                DiscardNoteIfExists("The note couldn't be saved.");
            }
        }
    });

    private VaultNoteListAdapter adapter;
    private Toolbar vaultTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_note_list);

        RecyclerView recyclerView = findViewById(R.id.noteVaultRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new VaultNoteListAdapter(this);
        adapter.setNoteListClickListener(this);

        recyclerView.setAdapter(adapter);

        NoteListItemDecoration decoration = new NoteListItemDecoration(getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);

        vaultNoteViewModel = new ViewModelProvider(this).get(VaultNoteViewModel.class);

        vaultNoteViewModel.getVaultNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable final List<NoteEntity> notes) {
                // update the cached copy of the notes in the adapter
                adapter.setNotes(notes);
            }
        });

        FloatingActionButton fab = findViewById(R.id.vaultListFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAddFabClick();
            }
        });

        vaultTopBar = findViewById(R.id.noteVaultTopBar);
        setSupportActionBar(vaultTopBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Vault Notes");
        }
    }

    public void OnAddFabClick() {
        // we should insert a blank note here
        // and we should send the data of the note id with the intent
        InsertBlankNote();

        // now we send the intent
        Intent intent = new Intent(VaultNoteListActivity.this, NoteEditActivity.class);
        intent.putExtra("CURRENT_NOTE_TYPE", NoteType.NOTE_VAULT.getValue());
        intent.putExtra("CURRENT_NOTE_ID", currentNoteId);
        startActivityForResult.launch(intent);
    }

    private void InsertBlankNote() {
        // basically we create a new blank note here, insert it into the database so we have the note id to refer to the images and voice recordings etc
        NoteEntity blankNote = new NoteEntity();
        blankNote.noteBody = "";
        blankNote.noteTitle = "";
        blankNote.noteType = NoteType.NOTE_VAULT.getValue();

        long noteId = vaultNoteViewModel.insertBlank(blankNote);
        currentNoteId = noteId;
        // now we have the id and we can pass it with the intent
    }

    @Override
    public void onNoteListItemClicked(View view, int position) {
        NoteEntity noteEntity = adapter.getVaultNotesList().get(position);

        currentNoteId = noteEntity.noteId;

        // now we send the intent
        Intent intent = new Intent(VaultNoteListActivity.this, NoteEditActivity.class);
        intent.putExtra("CURRENT_NOTE_BODY", noteEntity.noteBody);
        intent.putExtra("CURRENT_NOTE_TITLE", noteEntity.noteTitle);
        intent.putExtra("CURRENT_NOTE_ID", noteEntity.noteId);
        intent.putExtra("CURRENT_NOTE_TYPE", noteEntity.noteType);
        startActivityForResult.launch(intent);
    }

    private void DiscardNoteIfExists(String toastText) {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.noteId = currentNoteId;
        vaultNoteViewModel.delete(noteEntity);
        Toast.makeText(
                getApplicationContext(),
                toastText,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_vaults, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_vault_pin:
                OpenSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void OpenSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}

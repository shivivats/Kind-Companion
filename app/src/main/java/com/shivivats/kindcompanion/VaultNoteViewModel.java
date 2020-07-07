package com.shivivats.kindcompanion;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VaultNoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;

    private LiveData<List<NoteEntity>> vaultNotes;

    public VaultNoteViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        vaultNotes = noteRepository.getVaultNotes();
    }

    LiveData<List<NoteEntity>> getVaultNotes() {
        return vaultNotes;
    }

    public void insert(NoteEntity noteEntity) {
        noteRepository.insertNote(noteEntity);
    }

    public long insertBlank(NoteEntity blank) {
        long noteId = noteRepository.insertBlankNote(blank);
        return noteId;
    }

    public void update(NoteEntity noteEntity) {
        noteRepository.update(noteEntity);
    }

    public void delete(NoteEntity noteEntity) {
        noteRepository.delete(noteEntity);
    }
}

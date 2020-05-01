package com.shivivats.kindcompanion;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteEntityDao noteEntityDao;

    private LiveData<List<NoteTuple>> reminderNotes;

    private LiveData<List<NoteTuple>> vaultNotes;

    NoteRepository(Application application) {
        NoteDatabase db = NoteDatabase.getDatabase(application);
        noteEntityDao = db.noteEntityDao();
        reminderNotes = noteEntityDao.loadNotesByType(NoteType.NOTE_REMINDER.getValue());
        vaultNotes = noteEntityDao.loadNotesByType(NoteType.NOTE_VAULT.getValue());
    }

    LiveData<List<NoteTuple>> getReminderNotes() {
        return reminderNotes;
    }

    LiveData<List<NoteTuple>> getVaultNotes() {
        return vaultNotes;
    }

    void insertNote(NoteEntity noteEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            noteEntityDao.insertNotes(noteEntity);
        });
    }

}




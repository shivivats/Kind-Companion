package com.shivivats.kindcompanion;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class NoteRepository {

    private NoteEntityDao noteEntityDao;

    private LiveData<List<NoteEntity>> reminderNotes;

    private LiveData<List<NoteEntity>> vaultNotes;

    NoteRepository(Application application) {
        NoteDatabase db = NoteDatabase.getDatabase(application);
        noteEntityDao = db.noteEntityDao();
        reminderNotes = noteEntityDao.loadNotesByType(NoteType.NOTE_REMINDER.getValue());
        vaultNotes = noteEntityDao.loadNotesByType(NoteType.NOTE_VAULT.getValue());
    }

    LiveData<List<NoteEntity>> getReminderNotes() {
        return reminderNotes;
    }

    LiveData<List<NoteEntity>> getVaultNotes() {
        return vaultNotes;
    }

    void insertNote(NoteEntity noteEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            noteEntityDao.insertNotes(noteEntity);
        });
    }

    long insertBlankNote(NoteEntity blank) {
        long noteId=-1;

        Future<Long> insertBlankNoteFuture = NoteDatabase.databaseWriteExecutor.submit(() -> {
            long currId = noteEntityDao.insertBlankNote(blank);
            Long longCurrId = currId;
            return longCurrId;
        });

        try {
            noteId = insertBlankNoteFuture.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return noteId;
    }

    void update(NoteEntity noteEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            noteEntityDao.updateNotes(noteEntity);
        });
    }

    void delete(NoteEntity noteEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            noteEntityDao.deleteNotes(noteEntity);
        });
    }

}




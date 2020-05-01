package com.shivivats.kindcompanion;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteReminderViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;

    private LiveData<List<NoteTuple>> reminderNotes;

    public NoteReminderViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        reminderNotes = noteRepository.getReminderNotes();
    }

    LiveData<List<NoteTuple>> getReminderNotes() {return reminderNotes;}

    public void insert(NoteEntity noteEntity) { noteRepository.insertNote(noteEntity);}
}

package com.shivivats.kindcompanion;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// we need this class bc our regular noteentryviewmodel has a "long" parameter on top of the default "application" parameter

public class NoteEntryViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private long mCurrentNoteId;

    public NoteEntryViewModelFactory(Application application, long currentNoteId) {
        mApplication=application;
        mCurrentNoteId=currentNoteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NoteEntryViewModel(mApplication,mCurrentNoteId);
    }
}

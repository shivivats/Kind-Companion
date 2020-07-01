package com.shivivats.kindcompanion;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AudioRepository {

    private AudioEntityDao audioEntityDao;
    private LiveData<List<AudioEntity>> currentNoteAudio;

    AudioRepository(Application application, long currentNoteId) {
        NoteDatabase db = NoteDatabase.getDatabase(application);
        audioEntityDao = db.audioEntityDao();
        currentNoteAudio = audioEntityDao.loadAudioPerNote(currentNoteId);
    }

    LiveData<List<AudioEntity>> getCurrentNoteAudio() {
        return currentNoteAudio;
    }

    void insert(AudioEntity audioEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            audioEntityDao.insertAudio(audioEntity);
        });
    }

    void update(AudioEntity audioEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            audioEntityDao.updateAudio(audioEntity);
        });
    }

    void delete(AudioEntity audioEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            audioEntityDao.deleteAudio(audioEntity);
        });
    }
}

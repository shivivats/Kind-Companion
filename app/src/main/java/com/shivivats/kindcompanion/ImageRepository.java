package com.shivivats.kindcompanion;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ImageRepository {

    private ImageEntityDao imageEntityDao;
    private LiveData<List<ImageEntity>> currentNoteImages;

    ImageRepository(Application application, long currentNoteId) {
        NoteDatabase db = NoteDatabase.getDatabase(application);
        imageEntityDao = db.imageEntityDao();
        currentNoteImages = imageEntityDao.loadImagesPerNote(currentNoteId);
    }

    LiveData<List<ImageEntity>> getCurrentNoteImages() {
        return currentNoteImages;
    }

    void insert(ImageEntity imageEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> imageEntityDao.insertImages(imageEntity));
    }

    void update(ImageEntity imageEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> imageEntityDao.updateImages(imageEntity));
    }

    void delete(ImageEntity imageEntity) {
        NoteDatabase.databaseWriteExecutor.execute(() -> imageEntityDao.deleteImages(imageEntity));
    }
}

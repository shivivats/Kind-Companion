package com.shivivats.kindcompanion;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteEntryViewModel extends AndroidViewModel {
    private ImageRepository imageRepository;

    private LiveData<List<ImageEntity>> currentNoteImages;

    private long currentNoteId;

    public NoteEntryViewModel(Application application, long noteId) {
        super(application);
        imageRepository = new ImageRepository(application, noteId);
        //imageRepository.currentNoteId= noteId;
        currentNoteImages = imageRepository.getCurrentNoteImages();
        currentNoteId=noteId;
    }

    LiveData<List<ImageEntity>> getCurrentNoteImages() {
        return currentNoteImages;
    }



    public void insert(ImageEntity image) {
        imageRepository.insert(image);
    }

    public void update(ImageEntity image) {imageRepository.update(image);}

    public void delete(ImageEntity image) {imageRepository.delete(image);}

    public void setNoteId(int id) {
        currentNoteId=id;
    }
}

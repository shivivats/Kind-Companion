package com.shivivats.kindcompanion;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteEditViewModel extends AndroidViewModel {
    private ImageRepository imageRepository;
    private AudioRepository audioRepository;

    private LiveData<List<ImageEntity>> currentNoteImages;
    private  LiveData<List<AudioEntity>> currentNoteAudio;

    private long currentNoteId;

    public NoteEditViewModel(Application application, long noteId) {
        super(application);
        imageRepository = new ImageRepository(application, noteId);
        audioRepository = new AudioRepository(application, noteId);

        currentNoteImages = imageRepository.getCurrentNoteImages();
        currentNoteAudio = audioRepository.getCurrentNoteAudio();

        currentNoteId=noteId;
    }

    public void setNoteId(int id) {
        currentNoteId=id;
    }



    LiveData<List<ImageEntity>> getCurrentNoteImages() {
        return currentNoteImages;
    }

    public int getNumberOfNoteImages() {
        return currentNoteImages.getValue().size();
    }

    public void insertImages(ImageEntity image) {
        imageRepository.insert(image);
    }

    public void updateImages(ImageEntity image) {imageRepository.update(image);}

    public void deleteImages(ImageEntity image) {imageRepository.delete(image);}



    LiveData<List<AudioEntity>> getCurrentNoteAudio() {return  currentNoteAudio;}

    public int getNumberOfNoteAudio() {return  currentNoteAudio.getValue().size();}

    public void insertAudio(AudioEntity audioEntity) {audioRepository.insert(audioEntity);}

    public void updateAudio(AudioEntity audioEntity) {audioRepository.update(audioEntity);}

    public void deleteAudio(AudioEntity audioEntity) {audioRepository.delete(audioEntity);}

}

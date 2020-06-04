package com.shivivats.kindcompanion;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImageEntityDao {
    // simply get all images
    @Query("SELECT * FROM Note_Images")
    List<ImageEntity> getAll();

    // load images using the type
    @Query("SELECT * FROM Note_Images WHERE is_drawing = :isDrawing")
    List<ImageEntity> loadImagesByType(boolean isDrawing);

    // load images per note
    @Query("SELECT * FROM Note_Images WHERE image_note_id=:noteId")
    LiveData<List<ImageEntity>> loadImagesPerNote(long noteId);

    // Add one or more images to the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertImages(ImageEntity... images);

    // Delete one or more images from the database
    @Delete
    void deleteImages(ImageEntity... images);

    // Update one or more images in the database, matched using the primary key
    @Update
    void updateImages(ImageEntity... images);
}

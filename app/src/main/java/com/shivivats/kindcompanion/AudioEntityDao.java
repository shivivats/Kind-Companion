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
public interface AudioEntityDao {

    // simply get all audio
    @Query("SELECT * FROM Note_Audio")
    List<AudioEntity> getAll();

    // load audio using the type
    @Query("SELECT * FROM Note_Audio WHERE is_recording = :isRecording")
    List<AudioEntity> loadAudioByType(boolean isRecording);

    // load audio per note
    @Query("SELECT * FROM Note_Audio WHERE audio_note_id=:noteId")
    LiveData<List<AudioEntity>> loadAudioPerNote(long noteId);

    // Add one or more audio files to the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAudio(AudioEntity... audios);

    // Delete one or more audio files from the database
    @Delete
    void deleteAudio(AudioEntity... audios);

    // Update one or more audio files in the database, matched using the primary key
    @Update
    void updateAudio(AudioEntity... audios);
}

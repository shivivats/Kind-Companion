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
public interface NoteEntityDao {

    // WILL NEED TO ADD IMAGES AND AUDIO RECORDING IN THE FUTURE

    // simply get all notes
    @Query("SELECT note_title, note_body FROM Notes")
    List<NoteTuple> getAll();

    // load notes using the type
    @Query("SELECT note_title, note_body FROM Notes WHERE note_type = :noteType")
    LiveData<List<NoteTuple>> loadNotesByType(int noteType);

    // we're not gonna do a query for search bc that'd be too resource consuming
    // but just in case
    @Query("SELECT note_title, note_body FROM Notes WHERE note_title LIKE :searchTerm OR " +
            "note_body LIKE :searchTerm")
    List<NoteTuple> searchNotes(String searchTerm);

    // Add one or more notes to the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotes(NoteEntity... notes);

    // Delete one or more notes from the database
    @Delete
    void deleteNotes(NoteEntity... notes);

    // Update one or more notes in the database, matched using the primary key
    @Update
    void updateNotes(NoteEntity... notes);
}

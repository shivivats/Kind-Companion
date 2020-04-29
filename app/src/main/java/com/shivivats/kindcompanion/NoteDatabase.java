package com.shivivats.kindcompanion;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteEntityDao noteEntityDao();

    private static NoteDatabase INSTANCE;

    public static NoteDatabase getNoteDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "Notes_DB").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}

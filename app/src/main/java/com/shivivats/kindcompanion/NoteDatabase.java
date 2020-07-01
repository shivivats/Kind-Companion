package com.shivivats.kindcompanion;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NoteEntity.class, ImageEntity.class, AudioEntity.class}, version = 3, exportSchema = true)
@TypeConverters(Converters.class)
public abstract class NoteDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile NoteDatabase INSTANCE;
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                NoteEntityDao noteEntityDao = INSTANCE.noteEntityDao();
                ImageEntityDao imageEntityDao = INSTANCE.imageEntityDao();
                AudioEntityDao audioEntityDao = INSTANCE.audioEntityDao();

                // we just create a sample note here that says idk welcome to the app or sth
                NoteEntity sampleReminderNote = new NoteEntity();
                sampleReminderNote.noteTitle = "Reminders Preview";
                sampleReminderNote.noteBody = "This is a sample note displaying how a note is going to look like placed as a Reminder. \n Know that you are beautiful and you are loved <3";
                sampleReminderNote.noteType = NoteType.NOTE_REMINDER.getValue();
                noteEntityDao.insertNotes(sampleReminderNote);

                NoteEntity sampleVaultNote = new NoteEntity();
                sampleVaultNote.noteTitle = "Vault Preview";
                sampleVaultNote.noteBody = "This is a sample note displaying how a note is going to look like placed in the Vault. \n The vault is a safe place that can even be password protected.";
                sampleVaultNote.noteType = NoteType.NOTE_VAULT.getValue();
                noteEntityDao.insertNotes(sampleVaultNote);


            });
        }
    };

    static NoteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "Notes_DB")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract NoteEntityDao noteEntityDao();

    public abstract ImageEntityDao imageEntityDao();

    public abstract AudioEntityDao audioEntityDao();
}

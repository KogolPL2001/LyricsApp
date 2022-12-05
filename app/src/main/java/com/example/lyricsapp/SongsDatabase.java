package com.example.lyricsapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities={Song.class},version=1,exportSchema = false)
public abstract class SongsDatabase extends RoomDatabase {
    private static SongsDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor= Executors.newSingleThreadExecutor();
    public abstract SongDao songDao();
    static SongsDatabase getDatabase(final Context context){
        if(databaseInstance==null){
            databaseInstance= Room.databaseBuilder(context.getApplicationContext(),SongsDatabase.class,"songs_database").build();
        }
        return databaseInstance;
    }
}

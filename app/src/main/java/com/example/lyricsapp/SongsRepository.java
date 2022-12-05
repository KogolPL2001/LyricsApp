package com.example.lyricsapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SongsRepository {
    private final SongDao songDao;
    private final LiveData<List<Song>> songs;

    SongsRepository(Application application){
        SongsDatabase database=SongsDatabase.getDatabase(application);
        songDao= database.songDao();
        songs=songDao.findAll();
    }
    LiveData<List<Song>> findAllSongs(){ return songs; }

    void insert(Song song){
        SongsDatabase.databaseWriteExecutor.execute(()->songDao.insert(song));
    }
    void update(Song song){
        SongsDatabase.databaseWriteExecutor.execute(()->songDao.update(song));
    }
    void delete(Song song){
        SongsDatabase.databaseWriteExecutor.execute(()->songDao.delete(song));
    }
}

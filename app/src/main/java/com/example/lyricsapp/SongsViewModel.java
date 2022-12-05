package com.example.lyricsapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SongsViewModel extends AndroidViewModel {
    private final SongsRepository songsRepository;
    private final LiveData<List<Song>> songs;
    public SongsViewModel(@NonNull Application application){
        super(application);
        songsRepository=new SongsRepository(application);
        songs=songsRepository.findAllSongs();
    }
    public LiveData<List<Song>> findAll(){return songs;}
    public void insert(Song song){songsRepository.insert(song);}
    public void update(Song song){songsRepository.update(song);}
    public void delete(Song song){songsRepository.delete(song);}
}

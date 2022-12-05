package com.example.lyricsapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Song song);
    @Update
    void update(Song song);
    @Delete
    void delete(Song song);
    @Query("DELETE FROM song")
    void deleteAll();
    @Query("SELECT * FROM song ORDER BY title")
    LiveData<List<Song>> findAll();
    @Query("SELECT * FROM song WHERE title LIKE :title")
    List<Song> findSongWithTitle(String title);
}


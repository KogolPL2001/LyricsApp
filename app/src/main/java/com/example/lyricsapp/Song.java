package com.example.lyricsapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Song")
public class Song {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;
    private String ytlink;
    private String Content;
    private String Chords;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Song(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getYtlink() {
        return ytlink;
    }

    public void setYtlink(String ytlink) {
        this.ytlink = ytlink;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getChords() {
        return Chords;
    }

    public void setChords(String chords) {
        Chords = chords;
    }
}

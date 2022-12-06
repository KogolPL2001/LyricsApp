package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SongDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_SONG_DETAILS = "SONG_DETAILS";
    private Song song;
    private TextView titleTextView;
    private TextView authorTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        song = (Song)getIntent().getSerializableExtra("EXTRA_SONG_DETAILS");
        setContentView(R.layout.activity_song_details);
        titleTextView=findViewById(R.id.song_title);
        authorTextView=findViewById(R.id.song_author);
        titleTextView.setText(getResources().getString(R.string.song_title)+" "+song.getTitle());
        authorTextView.setText(getResources().getString(R.string.song_author)+" "+song.getAuthor());
    }
}
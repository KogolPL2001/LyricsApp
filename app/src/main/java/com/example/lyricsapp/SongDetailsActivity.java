package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SongDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_SONG_DETAILS = "SONG_DETAILS";
    private Song song;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView lyricsTextView;
    YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        song = (Song)getIntent().getSerializableExtra("EXTRA_SONG_DETAILS");
        setContentView(R.layout.activity_song_details);
        titleTextView=findViewById(R.id.song_title);
        authorTextView=findViewById(R.id.song_author);
        lyricsTextView=findViewById(R.id.song_lyrics);
        titleTextView.setText(getResources().getString(R.string.song_title)+" "+song.getTitle());
        authorTextView.setText(getResources().getString(R.string.song_author)+" "+song.getAuthor());
        lyricsTextView.setText(song.getContent());
        String ytlink = song.getYtlink();
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        if(ytlink.equals(""))
            youTubePlayerView.setVisibility(View.GONE);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = getYouTubeId(ytlink);
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }
    private String getYouTubeId (String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }
}
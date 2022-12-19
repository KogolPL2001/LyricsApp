package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SongDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_SONG_DETAILS = "SONG_DETAILS";
    public static final int  EDIT_SONG_ACTIVITY_REQUEST_CODE=3;
    private Song song;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView lyricsTextView;
    private SongsViewModel songsViewModel;
    YouTubePlayerView youTubePlayerView;
    private Song editedSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songsViewModel = ViewModelProviders.of(this).get(SongsViewModel.class);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_song_details_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_song) {
            editedSong=song;
            Intent intent=new Intent(SongDetailsActivity.this,AddSongActivity.class);
            intent.putExtra(AddSongActivity.EXTRA_ADD_SONG_TITLE, song.getTitle());
            intent.putExtra(AddSongActivity.EXTRA_ADD_SONG_AUTHOR, song.getAuthor());
            intent.putExtra(AddSongActivity.EXTRA_ADD_SONG_CONTENT, song.getContent());
            intent.putExtra(AddSongActivity.EXTRA_ADD_SONG_YTLINK, song.getYtlink());
            startActivityForResult(intent,EDIT_SONG_ACTIVITY_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == EDIT_SONG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
                editedSong.setTitle(data.getStringExtra(AddSongActivity.EXTRA_ADD_SONG_TITLE));
                editedSong.setAuthor(data.getStringExtra(AddSongActivity.EXTRA_ADD_SONG_AUTHOR));
                editedSong.setYtlink(data.getStringExtra(AddSongActivity.EXTRA_ADD_SONG_YTLINK));
                editedSong.setContent(data.getStringExtra(AddSongActivity.EXTRA_ADD_SONG_CONTENT));
                songsViewModel.update(editedSong);
                titleTextView.setText(getResources().getString(R.string.song_title)+" "+editedSong.getTitle());
                authorTextView.setText(getResources().getString(R.string.song_author)+" "+editedSong.getAuthor());
                lyricsTextView.setText(editedSong.getContent());
                String ytlink = editedSong.getYtlink();
                if(ytlink.equals(""))
                    youTubePlayerView.setVisibility(View.GONE);
                else
                    youTubePlayerView.setVisibility(View.VISIBLE);
                editedSong = null;
            } else{
               Snackbar.make(findViewById(R.id.song_details_layout),
                                getString(R.string.empty_edit_not_saved),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
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
package com.example.lyricsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class AddSongActivity extends AppCompatActivity {

    public static final String EXTRA_ADD_SONG_TITLE = "ADD_SONG_TITLE";
    public static final String EXTRA_ADD_SONG_AUTHOR = "ADD_SONG_AUTHOR";
    public static final String EXTRA_ADD_SONG_CONTENT = "ADD_SONG_LYRICS";

    private EditText addTitleEditText;
    private EditText addAuthorEditText;
    private EditText addContentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);


        addTitleEditText = findViewById(R.id.add_song_title);
        addAuthorEditText = findViewById(R.id.add_song_author);
        addContentEditText = findViewById(R.id.add_song_content);
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(e -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(addTitleEditText.getText())
                    || TextUtils.isEmpty(addAuthorEditText.getText())|| TextUtils.isEmpty(addContentEditText.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String title = addTitleEditText.getText().toString();
                replyIntent.putExtra(EXTRA_ADD_SONG_TITLE, title);
                String author = addAuthorEditText.getText().toString();
                replyIntent.putExtra(EXTRA_ADD_SONG_AUTHOR, author);
                String content = addContentEditText.getText().toString();
                replyIntent.putExtra(EXTRA_ADD_SONG_CONTENT,content);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}
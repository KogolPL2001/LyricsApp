package com.example.lyricsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SongsViewModel songsViewModel;
    public static final int NEW_SONG_ACTIVITY_REQUEST_CODE=1;
    public static final int  SONG_DETAILS_ACTIVITY_REQUEST_CODE=2;
    final SongsAdapter adapter = new SongsAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        songsViewModel = ViewModelProviders.of(this).get(SongsViewModel.class);
        songsViewModel.findAll().observe(this, adapter::setBooks);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_SONG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Song song = new Song(data.getStringExtra(AddSongActivity.EXTRA_ADD_SONG_TITLE),
                    data.getStringExtra(AddSongActivity.EXTRA_ADD_SONG_AUTHOR),
                    data.getStringExtra(AddSongActivity.EXTRA_ADD_SONG_CONTENT),
                    data.getStringExtra(AddSongActivity.EXTRA_ADD_SONG_YTLINK));
            songsViewModel.insert(song);
        } else if(requestCode==SONG_DETAILS_ACTIVITY_REQUEST_CODE) {
            //empty
        }else {
                Snackbar.make(findViewById(R.id.main_layout),
                                getString(R.string.empty_not_saved),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater=getMenuInflater();
       inflater.inflate(R.menu.app_main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search_song);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_song));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_song) {
            Intent intent=new Intent(MainActivity.this,AddSongActivity.class);
            startActivityForResult(intent,NEW_SONG_ACTIVITY_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Songs holder
    private class SongsHolder extends RecyclerView.ViewHolder {
        private final TextView songTitleTextView;
        private final TextView songAuthorTextView;
        private Song song;
        View songItem = itemView.findViewById(R.id.song_item);

        public SongsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.songs_list_item, parent, false));

            songTitleTextView = itemView.findViewById(R.id.song_title);
            songAuthorTextView = itemView.findViewById(R.id.song_author);


            //Removing songs for now
            songItem.setOnLongClickListener(v -> {
                songsViewModel.delete(song);
                return true;
            });
        }

        public void bind(Song song) {
            songTitleTextView.setText(song.getTitle());
            songAuthorTextView.setText(song.getAuthor());
            this.song = song;
            songItem.setOnClickListener(v->{
                Intent intent = new Intent(MainActivity.this, SongDetailsActivity.class);
                intent.putExtra("EXTRA_SONG_DETAILS",song);
                startActivityForResult(intent, SONG_DETAILS_ACTIVITY_REQUEST_CODE);
            });
        }
    }

    //Songs adapter
    private class SongsAdapter extends RecyclerView.Adapter<SongsHolder> implements  Filterable{
        private List<Song> songs;
        private List<Song> songsListFull;


        @NonNull
        @Override
        public SongsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SongsHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(SongsHolder holder, int position) {
            if (songs != null) {
                Song song = songs.get(position);
                holder.bind(song);
             } else {
                Log.d("MainActivity", "No songs");
            }
        }

        public int getItemCount() {
            if (songs != null) {
                return songs.size();
            } else {
                return 0;
            }
        }

        void setBooks(List<Song> songs) {
            this.songs = songs;
            songsListFull = new ArrayList<>(songs);
            notifyDataSetChanged();
        }


        @Override
        public Filter getFilter() {
            return exampleFilter;
        }

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Song> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(songsListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Song song : songsListFull) {
                        if (song.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(song);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                songs.clear();
                songs.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
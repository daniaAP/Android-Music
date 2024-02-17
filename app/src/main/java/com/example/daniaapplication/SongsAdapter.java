package com.example.daniaapplication;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private List<Song> songs;
    private OnSongClickListener listener;
    public SongsAdapter(List<Song> songs) {
        this.songs = songs;
    }
    public SongsAdapter(List<Song> songs, OnSongClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }
    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songTitle.setText(song.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SongsAdapter.this.listener != null){
                    SongsAdapter.this.listener.onSongClick(song);
                }
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(SongsAdapter.this.listener != null){
                    SongsAdapter.this.listener.onSongLongClick(song);
                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView songTitle;
        // Define more views

        public SongViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            view = itemView;
        }
    }

    public void setListener(OnSongClickListener listener) {
        this.listener = listener;
    }
}

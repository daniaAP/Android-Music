package com.example.daniaapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import java.util.List;

class AlbumSongsArrayAdapter extends ArrayAdapter<Song> {
    private Context context;
    private int resource;

    public AlbumSongsArrayAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        Song song = getItem(position);
        if (song != null) {
            TextView tvPos = convertView.findViewById(R.id.position);
            tvPos.setText(""+(position+1));
            TextView tvName = convertView.findViewById(R.id.songname);
            tvName.setText(song.getName());
            TextView tvNone = convertView.findViewById(R.id.none2);
            tvNone.setText("                                                               ");
        }
        return convertView;
    }
}


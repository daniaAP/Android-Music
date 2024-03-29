package com.example.daniaapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


import java.util.List;

public class SongArrayAdapter extends ArrayAdapter<Song> {
    private Context context;
    private int resource;

    public SongArrayAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        setUpImageLoader();
        if (convertView == null)
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        Song song = getItem(position);
        if (song != null) {
            int defaultImage = this.context.getResources().getIdentifier(String.valueOf(song.getImage()), null, this.context.getPackageName());
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .showImageOnLoading(defaultImage).build();
            ImageView imageView = convertView.findViewById(R.id.image);
            imageLoader.displayImage(String.valueOf(song.getImage()), imageView, options);
            TextView tvName = convertView.findViewById(R.id.name);
            tvName.setText(song.getName());
            TextView tvDesc = convertView.findViewById(R.id.desc);

            tvDesc.setText(song.getArtist());
            TextView tvNone = convertView.findViewById(R.id.none);
            tvNone.setText("        ");
        }
        return convertView;
    }

    private void setUpImageLoader() {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
}


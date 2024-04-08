package com.example.daniaapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    private List<Album> albumList;
    private OnAlbumClickListener onAlbumClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAlbum;
        private final TextView textViewArtist;
        private final ImageView imageViewAlbum;
        private final View view;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textViewAlbum = view.findViewById(R.id.textViewAlbum);
            this.textViewArtist = view.findViewById(R.id.textViewArtist);
            this.imageViewAlbum = view.findViewById(R.id.imageViewAlbum);
            // Define click listener for the ViewHolder's View
        }

        public TextView getTextViewAlbum() {
            return textViewAlbum;
        }

        public TextView getTextViewArtist() {
            return textViewArtist;
        }

        public ImageView getImageViewAlbum() {
            return imageViewAlbum;
        }

        public View getView() {
            return view;
        }
    }

    public HomeAdapter(Context context, List<Album> albumList) {
        this(albumList);
        if (context instanceof OnAlbumClickListener) {
            this.onAlbumClickListener = (OnAlbumClickListener) context;
        }

    }

    public HomeAdapter(List<Album> albumList) {
        this.albumList = albumList;
    }

    public HomeAdapter() {
        albumList = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Album album = this.albumList.get(position);
        viewHolder.getTextViewAlbum().setText(album.getName());
        viewHolder.getTextViewArtist().setText(album.getArtist());
        viewHolder.getImageViewAlbum().setImageResource(album.getImage());

//        if (position % 2 == 0) {
//            viewHolder.getView().setBackgroundResource(R.color.gray);
//        } else {
//            viewHolder.getView().setBackgroundResource(R.color.white);
//        }

        if (this.onAlbumClickListener != null) {
            viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeAdapter.this.onAlbumClickListener.onAlbumClick(album);
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.albumList.size();
    }


}

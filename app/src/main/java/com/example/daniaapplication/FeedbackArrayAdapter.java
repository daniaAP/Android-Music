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

class FeedbackArrayAdapter extends ArrayAdapter<FeedBack> {
    private Context context;
    private int resource;

    public FeedbackArrayAdapter(@NonNull Context context, int resource, @NonNull List<FeedBack> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        FeedBack feedBack = getItem(position);
        if (feedBack != null) {
            TextView usern = convertView.findViewById(R.id.user_list);
            usern.setText(feedBack.getUsername());
            TextView feed = convertView.findViewById(R.id.feed_list);
            feed.setText(feedBack.getText());
        }
        return convertView;
    }
}

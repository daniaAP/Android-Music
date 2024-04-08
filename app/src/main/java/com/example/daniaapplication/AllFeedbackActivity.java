package com.example.daniaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllFeedbackActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<FeedBack> feedBacks;
    ArrayAdapter<FeedBack> arrayAdapter;
    FirebaseDatabase database;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_feedback);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        listView = findViewById(R.id.feedbacks);
        swipeRefreshLayout.setRefreshing(true);
        refreshContent();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the content goes here
                refreshContent();
            }
        });
        feedBacks = new ArrayList<>();
    }

    private void refreshContent() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Feedbacks");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedBacks = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    FeedBack feedBack = data.getValue(FeedBack.class);
                    feedBacks.add(feedBack);
                }
                arrayAdapter = new FeedbackArrayAdapter(AllFeedbackActivity.this, R.layout.feedback_list, feedBacks);
                listView.setAdapter(arrayAdapter);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
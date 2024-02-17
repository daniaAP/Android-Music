package com.example.daniaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_feedback);
        listView = findViewById(R.id.feedbacks);
        feedBacks = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Feedbacks");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()){
                    FeedBack feedBack=data.getValue(FeedBack.class);
                    feedBacks.add(feedBack);
                }
                arrayAdapter = new FeedbackArrayAdapter(AllFeedbackActivity.this, R.layout.feedback_list, feedBacks);
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
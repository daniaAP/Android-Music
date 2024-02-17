package com.example.daniaapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    TextView myfeed;
    EditText feed;
    Button sub;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myfeed = findViewById(R.id.myfeed);
        myfeed.setOnClickListener(this);
        feed = findViewById(R.id.feed);
        sub = findViewById(R.id.sub);
        sub.setOnClickListener(this);
        DatabaseReference ref = database.getReference("Feedbacks").child(mAuth.getCurrentUser().getUid().toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String f = snapshot.child("text").getValue(String.class);
                String text = "Your feedback " + f;
                myfeed.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref_user = database.getReference("Users").child(mAuth.getCurrentUser().getUid().toString()).child("username");
        ref_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == sub) {
            String text = feed.getText().toString();
            FeedBack feedBack = new FeedBack(text, username);
            database.getReference("Feedbacks").child(mAuth.getCurrentUser().getUid().toString()).setValue(feedBack);
        }
        else if(v == myfeed){
            Intent i = new Intent(this, AllFeedbackActivity.class);
            startActivity(i);
        }
    }
}
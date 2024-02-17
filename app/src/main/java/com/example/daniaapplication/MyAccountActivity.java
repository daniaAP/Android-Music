package com.example.daniaapplication;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private EditText nameInput;
    private TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        // Initialize your buttons and fields
        Button backButton = findViewById(R.id.back_button);
        Button updateButton = findViewById(R.id.update_button);
        nameInput = findViewById(R.id.name_input);
        email = findViewById(R.id.email);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        loadFullName();

        // TODO get value from
        // Set up the button click listeners
        backButton.setOnClickListener(view -> {
            // Handle back button click
            finish();
        });

        updateButton.setOnClickListener(view -> {
            final String fullname = nameInput.getText().toString();
            saveFullname(fullname);
            finish();
        });
    }

    private void loadFullName(){
        final String uid = mAuth.getCurrentUser().getUid().toString();
        DatabaseReference dbref = database.getReference("Users").child(uid);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> userObj = (HashMap<String, Object>) snapshot.getValue();
                String fullname = "";
                if(userObj.containsKey("fullname")){
                    fullname = String.valueOf(userObj.get("fullname"));
                }
                nameInput.setText(fullname);
                email.setText(String.valueOf(userObj.get("email")));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(MyAccountActivity.class.getSimpleName(), "Error retrieving data!!");
            }
        });
    }


    private void saveFullname(String fullname){
        final String uid = mAuth.getCurrentUser().getUid().toString();
        DatabaseReference dbref = database.getReference("Users").child(uid);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> userObj = (HashMap<String, Object>) snapshot.getValue();
                userObj.put("fullname", fullname);
                database.getReference("Users").child(uid).setValue(userObj);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(MyAccountActivity.class.getSimpleName(), "Error retrieving data!!");
            }
        });
    }
}

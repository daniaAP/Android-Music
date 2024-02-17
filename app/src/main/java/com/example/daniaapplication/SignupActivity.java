package com.example.daniaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText passw, emailS, username;
    private FirebaseAuth mAuth;
    private Button pre, signI;
    FirebaseDatabase database;
    private ProgressBar progressBarSignUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        passw = findViewById(R.id.pass);
        emailS = findViewById(R.id.email2);
        database = FirebaseDatabase.getInstance();
        username = findViewById(R.id.etUsername);
        pre = findViewById(R.id.prev);
        signI = findViewById(R.id.signin2);

        progressBarSignUp = findViewById(R.id.progressBarSignUp);

        mAuth = FirebaseAuth.getInstance();

        pre.setOnClickListener(this);
        signI.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.prev) {
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.signin2) {
            createAccount();
        }
    }

    public void createAccount() {
        if (emailS.getText().toString().isEmpty())
            return;
        if (passw.getText().toString().isEmpty())
            return;


        disableForm();
        progressBarSignUp.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailS.getText().toString(), passw.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String email = emailS.getText().toString();
                            String usern = username.getText().toString();
                            User user = new User(usern,email);
                            database.getReference("Users").child(mAuth.getCurrentUser().getUid().toString()).setValue(user);
                            startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(SignupActivity.this, "Account creation failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBarSignUp.setVisibility(View.INVISIBLE);
                            enableForm();
                        }
                    }
                });
    }

    private void enableForm() {
        pre.setEnabled(true);
        signI.setEnabled(true);
    }

    private void disableForm() {
        pre.setEnabled(false);
        signI.setEnabled(false);
    }
}







                  




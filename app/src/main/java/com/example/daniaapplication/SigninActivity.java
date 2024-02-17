package com.example.daniaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {
    private EditText Email, password;
    private TextView andr, acc;
    private Button signin;
    private CheckBox save;
    private SharedPreferences sp;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Email = findViewById(R.id.email);
        password = findViewById(R.id.etPass);
        andr = findViewById(R.id.textView);
        acc = findViewById(R.id.tvSignin);
        signin = findViewById(R.id.signin);
        save = findViewById(R.id.save);
        sp = getApplicationContext().getSharedPreferences("sp", 0);
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        String email = sp.getString("email", null);
        String pass = sp.getString("pass", null);
        if (email != null && pass != null) {
            Email.setText(email);
            password.setText(pass);
        }

        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(i);
            }

        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().equals("")) {
                    Toast.makeText(SigninActivity.this, "Error! Empty Fields...", Toast.LENGTH_LONG).show();
                } else if (password.getText().toString().equals("")) {
                    Toast.makeText(SigninActivity.this, "Error! Empty Fields...", Toast.LENGTH_LONG).show();
                } else {
                    if(save.isChecked()){
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", Email.getText().toString());
                        editor.putString("pass",password.getText().toString());
                        editor.apply();
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    disableForm();
                    signIn(Email.getText().toString(), password.getText().toString());
                }
            }
        });

        Email.setText("kda@gmail.com");
        password.setText("123456789");
    }

    public void signIn(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SigninActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            enableForm();
                        }
                    }
                });
    }
    private void enableForm(){
        signin.setEnabled(true);
        acc.setEnabled(true);
    }
    private void disableForm(){
        signin.setEnabled(false);
        acc.setEnabled(false);
    }
}

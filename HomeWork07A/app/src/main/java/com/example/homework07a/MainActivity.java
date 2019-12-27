package com.example.homework07a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    EditText email;
    EditText password;
    public static final int REQ_CODE = 2;
    Button login;
    Button Signup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar pb_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        Signup = findViewById(R.id.signup);
        pb_login=findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        Context ctx = getApplicationContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        pb_login.setVisibility(View.INVISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                pb_login.setVisibility(View.VISIBLE);
                signIn(emailStr,passwordStr);
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateProfile.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Login Successful",
                                    Toast.LENGTH_SHORT).show();
                            DocumentReference docRef = db.collection("users").document(user.getEmail());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            User user = new User(document.getData());
                                            Gson gson = new Gson();
                                            String jsonString = gson.toJson(user);
                                            Context ctx = getApplicationContext();
                                            SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("Key", jsonString);
                                            editor.commit();
                                            pb_login.setVisibility(View.INVISIBLE);
                                            Log.d("document Data", "DocumentSnapshot data: " + document.getData());
                                            Intent intent = new Intent(MainActivity.this, TripsActivity.class);
                                            startActivity(intent);

                                        } else {
                                            Log.d("document Data", "No such document");
                                            pb_login.setVisibility(View.INVISIBLE);
                                            Toast.makeText(MainActivity.this, "No user data found", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.d("document Data", "get failed with ", task.getException());
                                        pb_login.setVisibility(View.INVISIBLE);
                                        Toast.makeText(MainActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            pb_login.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
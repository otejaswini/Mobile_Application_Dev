package com.example.firebasetest;
// By Tejaswini Oduru
// student ID: 801151367
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button addMovie;
    Button editMovie;
    Button deleteMovie;
    Button showListbyYear;
    Button showListbyRating;
    public static final int REQ_CODE = 2;
    public static final int EDIT_REQ_CODE = 3;
    public static final String TAG_MOVIE = "addMovie";
    public static final String TAG_EDIT_MOVIE = "editMovie";
    ArrayList<movie> arrayMovie = new ArrayList<movie>();
    int index = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> arrayList = new ArrayList<>();
    movie delmovie;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Favourite Movies");
        addMovie = findViewById(R.id.addMovie);
        editMovie = findViewById(R.id.editMovie);
        deleteMovie = findViewById(R.id.delMovie);
        showListbyYear = findViewById(R.id.showListbyYear);
        showListbyRating = findViewById(R.id.showListbyRating);



        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMovie.class);
                startActivityForResult(intent, REQ_CODE);

            }
        });
        db.collection("movies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                arrayMovie.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    movie movieObj = new movie(documentSnapshot.getData());
                    arrayMovie.add(movieObj);
                }
            }
        });
        editMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i = 0; i < arrayMovie.size(); i++) {
                    arrayList.add(arrayMovie.get(i).getName());
                }
                if (arrayList.size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Pick a Movie")
                            .setItems(arrayList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    movie editMovie = (movie) arrayMovie.get(which);
                                    index = which;
                                    Intent i = new Intent(MainActivity.this, EditMovie.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("edit", editMovie);
                                    bundle.putInt("index",which);
                                    i.putExtra("Edit", bundle);
                                    startActivityForResult(i, EDIT_REQ_CODE);

                                }
                            });
                    builder.create();
                    builder.show();
                } else {
                    Toast.makeText(MainActivity.this, "There must be atleast one movie to perform edit", Toast.LENGTH_LONG).show();
                }

            }


        });

        deleteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> arrayListDel = new ArrayList<String>();

                for (int i = 0; i < arrayMovie.size(); i++) {
                    arrayListDel.add(arrayMovie.get(i).getName());
                }

                if (arrayListDel.size() != 0) {
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Pick a Movie").setItems(arrayListDel.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final int delindex = which;
                            delmovie = arrayMovie.get(delindex);
                            index = delindex;
                            db.collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        arrayList.clear();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            arrayList.add(document.getId());
                                        }

                                        db.collection("movies").document(arrayList.get(delindex))
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("deleted", "DocumentSnapshot successfully deleted!");
                                                        Toast.makeText(MainActivity.this, "Movie is Deleted", Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("no", "Error deleting document", e);
                                                    }
                                                });
                                    }
                                }
                            });
                        }

                    });
                    builder.create();
                    builder.show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No movies to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        showListbyRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayMovie.size() != 0) {
                    Intent intent = new Intent(MainActivity.this,moviebyRating.class);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "There must be atleast two movies to perform compare", Toast.LENGTH_LONG).show();
                }
            }
        });
        showListbyYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arrayMovie.size() != 0) {
                    Intent i = new Intent(MainActivity.this,moviebyYear.class);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(i);
                }
                else {
                    Toast.makeText(MainActivity.this, "There must be atleast two movies to perform compare", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == EDIT_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Movie has been edited", Toast.LENGTH_SHORT).show();
            }
        }

    }
}


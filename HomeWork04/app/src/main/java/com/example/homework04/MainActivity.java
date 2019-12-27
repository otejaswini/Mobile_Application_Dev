/*Group 34
*Name : Tejaswini Oduru
*Name : Sumit Kawale
* */
package com.example.homework04;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.FocusFinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    int index =0;

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
                Intent intent = new Intent(MainActivity.this, addMovie.class);
                startActivityForResult(intent, REQ_CODE);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Pick a Movie")

                            .setItems(arrayListDel.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    arrayMovie.remove(which);
                                    Toast.makeText(MainActivity.this, "Movie is Deleted", Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.create();
                    builder.show();
                } else {
                    Toast.makeText(MainActivity.this, "There must be atleast one movie to perform edit", Toast.LENGTH_LONG).show();
                }
            }
        });
        editMovie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i = 0; i < arrayMovie.size(); i++) {
                    //            Log.d("","");
                    arrayList.add(arrayMovie.get(i).getName());
                }
                if (arrayList.size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Pick a Movie")

                            .setItems(arrayList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    movie editMovie = (movie) arrayMovie.get(which);
                                    index = which;
                                    Intent i = new Intent(MainActivity.this, editMovie.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("edit", editMovie);
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

        showListbyYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayMovie.size() != 0) {
                    Intent i = new Intent("com.example.homework04.intent.action.MOVIEBYYEAR");
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("year", arrayMovie);
                    i.putExtra("year", bundle);
                    startActivity(i);
                }
                else {
                    Toast.makeText(MainActivity.this, "There must be atleast two movies to perform compare", Toast.LENGTH_LONG).show();
                }

            }
        });

        showListbyRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayMovie.size() != 0) {
                    Intent intent = new Intent("com.example.homework04.intent.action.MOVIEBYRATING");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("rating", arrayMovie);
                    intent.putExtra("rating", bundle);
                    startActivity(intent);
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
                final Bundle extrasFromMain = data.getExtras().getBundle(TAG_MOVIE);
                movie getMovie = (movie) extrasFromMain.getParcelable("ADDMOVIE");
                arrayMovie.add(getMovie);
                Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == EDIT_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                final Bundle extrasFromMain_edit = data.getExtras().getBundle(TAG_EDIT_MOVIE);
                movie getMovie = (movie) extrasFromMain_edit.getParcelable("EDITMOVIE");
                arrayMovie.set(index, getMovie);
                Toast.makeText(this, "Movie has been edited", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

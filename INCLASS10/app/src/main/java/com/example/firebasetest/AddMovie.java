package com.example.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddMovie extends AppCompatActivity {
    EditText addName;
    EditText addDescription;
    Spinner spinnerGenre;
    SeekBar sbRating;
    EditText addYear;
    EditText Imdb;
    TextView ratingvalue;
    String[] dropDown = {"Action", "Animation", "Comedy", "Documentary", "Family", "Horror",
            "Crime", "Others"};
    String drop = null;
    int sbValue = 0;
    Button add_Movie;
    ArrayList<movie> movieArrayList = new ArrayList<movie>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        setTitle("Add Movie");
        addName = findViewById(R.id.name);
        addDescription = findViewById(R.id.description);
        spinnerGenre = findViewById(R.id.spinner_genre);
        sbRating = findViewById(R.id.seekBar_add);
        addYear = findViewById(R.id.addyear);
        Imdb = findViewById(R.id.addimdb);
        add_Movie=findViewById(R.id.button_add);
        ratingvalue = findViewById(R.id.ratevalue_add);
        sbRating.setMin(0);
        sbRating.setMax(5);
        sbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sbRating.setProgress(progress);
                sbValue = progress;
                ratingvalue.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        add_Movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = addName.getText().toString();
                String Description = addDescription.getText().toString();
                String Year = addYear.getText().toString();
                String IMDB = Imdb.getText().toString();
                if(Name != null && Description != null && drop != null && sbValue != 0 && Year !=null && IMDB != null  )
                {

                    movie Movie = new movie(Name, Description, drop, sbValue, Year, IMDB);
                    Map<String,Object> MapMovie = Movie.toMovieMap();
                    db.collection("movies").document(Name)
                     .set(MapMovie)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                    }
                                    else{
                                        Log.d("demo", task.getException().toString());
                                    }
                                }
                            });

                    Intent intent = new Intent(AddMovie.this, MainActivity.class);
                    setResult(AddMovie.RESULT_OK, intent);
                    finish();
                } else {
                    if(Name == null)
                    {
                        Toast.makeText(AddMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(Description == null)
                    {
                        Toast.makeText(AddMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(drop==null)
                    {
                        Toast.makeText(AddMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(sbValue==0)
                    {
                        Toast.makeText(AddMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(Year==null)
                    {
                        Toast.makeText(AddMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(IMDB == null)
                    {
                        Toast.makeText(AddMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dropDown);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(arrayAdapter);
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                drop = value;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }
}

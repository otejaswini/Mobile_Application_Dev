package com.example.homework04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.homework04.MainActivity.TAG_MOVIE;

public class addMovie extends AppCompatActivity {
    EditText editName;
    EditText editDescription;
    Spinner spinnerGenre;
    SeekBar sbRating;
    EditText editYear;
    EditText editImdb;
    TextView ratevalue;
    String[] dropDown = {"Action", "Animation", "Comedy", "Documentary", "Family", "Horror",
            "Crime", "Others"};
    String drop = null;
    int sbValue = 0;
    Button add_Movie;
    ArrayList<movie> movieArrayList = new ArrayList<movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        setTitle("Add Movie");
        editName = findViewById(R.id.editname);
        editDescription = findViewById(R.id.editdescription);
        spinnerGenre = findViewById(R.id.spinnergenre);
        sbRating = findViewById(R.id.seekBar);
        editYear = findViewById(R.id.edityear);
        editImdb = findViewById(R.id.editimdb);
        add_Movie=findViewById(R.id.button_add);
        ratevalue = findViewById(R.id.ratevalue);
        sbRating.setMin(0);
        sbRating.setMax(5);
        sbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sbRating.setProgress(progress);
                sbValue = progress;
                ratevalue.setText(progress+"");
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
                String Name = editName.getText().toString();
                String Description = editDescription.getText().toString();
                String Year = editYear.getText().toString();
                String IMDB = editImdb.getText().toString();
                if(Name != null && Description != null && drop != null && sbValue != 0 && Year !=null && IMDB != null  )
                {
                    movie Movie = new movie(Name, Description, drop, sbValue, java.time.Year.parse(Year), IMDB);
                    movieArrayList.add(Movie);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("ADDMOVIE",Movie);
                    Intent intent = new Intent(addMovie.this, MainActivity.class);
                    intent.putExtra(TAG_MOVIE, bundle);
                    setResult(addMovie.RESULT_OK, intent);
                    finish();

                } else {

                    if(Name == null)
                    {
                        Toast.makeText(addMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(Description == null)
                    {
                        Toast.makeText(addMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();

                    }
                    if(drop==null)
                    {
                        Toast.makeText(addMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(sbValue==0)
                    {
                        Toast.makeText(addMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();

                    }
                    if(Year==null)
                    {
                        Toast.makeText(addMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(editImdb == null) {
                        Toast.makeText(addMovie.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
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
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}

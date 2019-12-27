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

import static com.example.homework04.MainActivity.TAG_EDIT_MOVIE;

public class editMovie extends AppCompatActivity {
    EditText editName;
    EditText editDescription;
    Spinner spinnerGenre;
    SeekBar sbRating;
    EditText editYear;
    EditText editImdb;
    TextView rate_value;
    String[] dropDown = {"Action", "Animation", "Comedy", "Documentary", "Family", "Horror",
            "Crime", "Others"};
    String drop = null;
    int sbValue = 0;
    Button editMovie;
    ArrayList<movie> movieArrayList = new ArrayList<movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle("Edit Movie");
        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        sbRating = findViewById(R.id.seekBaredit);
        editYear = findViewById(R.id.editYear);
        editImdb = findViewById(R.id.editImdb);
        editMovie=findViewById(R.id.button_edit);
        rate_value = findViewById(R.id.rate_value);
        sbRating.setMin(0);
        sbRating.setMax(5);
        sbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sbRating.setProgress(progress);
                sbValue = progress;
                rate_value.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ArrayAdapter<String> arrayAdapter_edit = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, dropDown);
        arrayAdapter_edit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(arrayAdapter_edit);
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                drop = selectedValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Bundle extrasFromMain = getIntent().getExtras().getBundle("Edit");
        movie edit_Movie = (movie) extrasFromMain.getParcelable("edit");
        if (edit_Movie != null) {
            editName.setText(edit_Movie.getName());
            editDescription.setText(edit_Movie.getDescription());
            editYear.setText(edit_Movie.getYear().toString());
            editImdb.setText(edit_Movie.getImdb());
            sbRating.setProgress(edit_Movie.getRating());
            if (edit_Movie.getGenre() != null) {
                int spinnerPosition = arrayAdapter_edit.getPosition(edit_Movie.getGenre());
                spinnerGenre.setSelection(spinnerPosition);
            }
        }

        editMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = editName.getText().toString();
                String Description = editDescription.getText().toString();
                String Year = editYear.getText().toString();
                String Imdb = editImdb.getText().toString();
                if (Name.length() != 0 && Description.length() != 0 && Year.length() != 0 && Imdb.length() != 0 && sbValue != 0 && drop.length() != 0) {
                    movie Movie = new movie(Name, Description, drop, sbValue, java.time.Year.parse(Year), Imdb);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("EDITMOVIE", Movie);
                    Intent intent = new Intent(com.example.homework04.editMovie.this, MainActivity.class);
                    intent.putExtra(TAG_EDIT_MOVIE, bundle);
                    setResult(com.example.homework04.editMovie.RESULT_OK, intent);
                    finish();

                } else {

                    if (Name.length() == 0) {
                        editName.setError("Name cannot be empty");

                    }
                    if (Description.length() == 0) {
                        editDescription.setError("Description cannot be empty!");

                    }
                    if (drop.length() == 0) {
                        Toast.makeText(com.example.homework04.editMovie.this, "Genre cannot be empty", Toast.LENGTH_SHORT).show();

                    }
                    if (sbValue == 0) {
                        Toast.makeText(com.example.homework04.editMovie.this, "Rating cannot be zero", Toast.LENGTH_SHORT).show();

                    }
                    if (Year.length() == 0) {
                        editYear.setError("Year cannot be empty");
                    }
                    if (Imdb.length() == 0) {
                        editImdb.setError("Imdb link cannot be empty");

                    }
                }

            }
        });
    }
}

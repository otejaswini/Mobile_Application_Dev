package com.example.homework04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class moviebyYear extends AppCompatActivity {
    TextView tv_title;
    TextView tv_des;
    TextView tv_genre;
    TextView tv_rating;
    TextView tv_year;
    TextView tv_imdb;
    ImageView firstImage;
    ImageView lastImage;
    ImageView previousImage;
    ImageView nextImage;
    Button finish;
    ArrayList<movie> arrayListRating = new ArrayList<movie>();
    int Index = 0;

    protected void setValues(int i) {
        tv_title = findViewById(R.id.tv_title);
        tv_des = findViewById(R.id.tv_des);
        tv_genre = findViewById(R.id.tv_genre);
        tv_rating = findViewById(R.id.tv_Rating);
        tv_year = findViewById(R.id.tv_Year);
        tv_imdb = findViewById(R.id.tv_Imdb);

        tv_title.setText(arrayListRating.get(i).getName());
        tv_des.setText(arrayListRating.get(i).getDescription());
        tv_genre.setText(arrayListRating.get(i).getGenre());
        tv_rating.setText(String.valueOf(arrayListRating.get(i).getRating()));
        tv_year.setText(String.valueOf(arrayListRating.get(i).getYear()));
        tv_imdb.setText(arrayListRating.get(i).getImdb());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieby_rating);
        setTitle("Movies By Rating");

        tv_title = findViewById(R.id.tv_title);
        tv_des = findViewById(R.id.tv_des);
        tv_genre = findViewById(R.id.tv_genre);
        tv_rating = findViewById(R.id.tv_Rating);
        tv_year = findViewById(R.id.tv_Year);
        tv_imdb = findViewById(R.id.tv_Imdb);
        firstImage = findViewById(R.id.img_first_rating);
        lastImage = findViewById(R.id.img_last_rating);
        previousImage = findViewById(R.id.img_previous_rating);
        nextImage = findViewById(R.id.img_next_rating);
        finish = findViewById(R.id.Finish);
        final Bundle extrasFromMain = getIntent().getExtras().getBundle("year");
        ArrayList<movie> arrayRating = extrasFromMain.getParcelableArrayList("year");


        Collections.sort(arrayRating, new Comparator<movie>() {
            @Override
            public int compare(movie o1, movie o2) {
                return Integer.parseInt(String.valueOf(o1.getYear())) - Integer.parseInt(String.valueOf(o2.getYear()));
            }
        });
        arrayListRating = arrayRating;
        if (arrayRating.size() != 0) {
            setValues(0);
        }
        firstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValues(0);
                Index = 0;
            }

        });
        lastImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = arrayListRating.size() - 1;
                setValues(i);
                Index = i;
            }
        });

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Index + 1;
                Index = i;
                if (i >= arrayListRating.size()) {
                    i = 0;
                    Index = 0;
                }
                setValues(i);
            }
        });

        previousImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Index - 1;
                Index = i;
                if (i < 0) {
                    i = arrayListRating.size() - 1;
                    Index = i;
                }
                setValues(i);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(moviebyYear.this, MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });

    }
}

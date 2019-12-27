package com.example.firebasetest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

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
    ArrayList<movie> arrayListYear = new ArrayList<movie>();
    int Index = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void setValues(int i) {

        tv_title.setText(arrayListYear.get(i).getName());
        tv_des.setText(arrayListYear.get(i).getDescription());
        tv_genre.setText(arrayListYear.get(i).getGenre());
        tv_rating.setText(String.valueOf(arrayListYear.get(i).getRating()));
        tv_year.setText(String.valueOf(arrayListYear.get(i).getYear()));
        tv_imdb.setText(arrayListYear.get(i).getImdb());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieby_year);
        setTitle("Movies By Year");

        tv_title = findViewById(R.id.tv_title_year);
        tv_des = findViewById(R.id.tv_des_year);
        tv_genre = findViewById(R.id.tv_genre_year);
        tv_rating = findViewById(R.id.tv_Rating_year);
        tv_year = findViewById(R.id.tv_Yearsort);
        tv_imdb = findViewById(R.id.tv_Imdb_year);
        firstImage = findViewById(R.id.img_first_year);
        lastImage = findViewById(R.id.img_last_year);
        previousImage = findViewById(R.id.img_previous_year);
        nextImage = findViewById(R.id.img_next_year);
        finish = findViewById(R.id.Finish_year);

        db.collection("movies").orderBy("Rating", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        movie movieOrder = new movie(documentSnapshot.getData());
                        arrayListYear.add(movieOrder);
                        if (arrayListYear.size() != 0) {
                            setValues(0);
                        }
                    }

                }
                else{
                    Log.d("Error", task.getException().toString());
                }
            }
        });

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
                int i = arrayListYear.size() - 1;
                setValues(i);
                Index = i;
            }
        });

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Index + 1;
                Index = i;
                if (i >= arrayListYear.size()) {
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
                    i = arrayListYear.size() - 1;
                    Index = i;
                }
                setValues(i);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

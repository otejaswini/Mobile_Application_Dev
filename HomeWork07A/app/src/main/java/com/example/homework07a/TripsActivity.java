package com.example.homework07a;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TripsActivity extends AppCompatActivity {
    ImageView Editprofile;
    Button addNewtrip;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    final static int REQ_CODE=5;
    ArrayList<Trips> tripArrayList= new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        Editprofile = findViewById(R.id.profileicon);
        addNewtrip = findViewById(R.id.addTrip);
        mRecyclerView = findViewById(R.id.rv_Trip);


        Editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripsActivity.this, EditProfile.class);
                startActivity(intent);
            }
        });

        addNewtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripsActivity.this, addTrip.class);
                startActivityForResult(intent,REQ_CODE);
            }
        });

        db.collection("trips").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                tripArrayList.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Trips trip = new Trips(documentSnapshot.getData());
                    tripArrayList.add(trip);
                }
                mLayoutManager = new LinearLayoutManager(TripsActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new RecyclerviewAdapter(tripArrayList, TripsActivity.this, mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
   if (requestCode == REQ_CODE) {
        if (data != null)
        {
            final Bundle extrasFromMain = data.getExtras().getBundle("tripBundle");
            Trips objTrip = (Trips) extrasFromMain.getSerializable("tripData");
        }

    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TripsActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null)
        {
            Intent intent = new Intent(TripsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}


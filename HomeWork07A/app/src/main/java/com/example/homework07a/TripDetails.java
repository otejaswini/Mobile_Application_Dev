package com.example.homework07a;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TripDetails extends AppCompatActivity {

    TextView trip_details_Name, place_Details, CreatedBy_Details;
    ImageView tripPhoto;
    Button  chat_button, join_Trip, delete_Trip;
    private FirebaseAuth mAuth;
    Trips objTrip;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> users;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> usersList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        setTitle("Trip Details");
        trip_details_Name = findViewById(R.id.trip_details_Name);
        place_Details = findViewById(R.id.place_Details);
        CreatedBy_Details = findViewById(R.id.CreatedBy_Details);
        tripPhoto = findViewById(R.id.tripPhoto);
        join_Trip = findViewById(R.id.join_Trip);
        delete_Trip = findViewById(R.id.delete_Trip);
        chat_button = findViewById(R.id.chat_button);
        mRecyclerView=findViewById(R.id.rv_members);
        join_Trip.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        final Bundle extrasFromMain = getIntent().getExtras().getBundle("OnClickTripBundle");
        objTrip = (Trips) extrasFromMain.getSerializable("OnClickTrip");
        if (objTrip != null) {
            users = new ArrayList<>();
            users = objTrip.getUsers();
            trip_details_Name.setText(objTrip.getTitle());
            place_Details.setText(objTrip.getPlace());
            CreatedBy_Details.setText(objTrip.getCreatedBy());
            Picasso.get().load(objTrip.getPhotoUrl()).into(tripPhoto);
             if (objTrip.users != null) {
                 usersList = new ArrayList(objTrip.getUsers());
                 FirebaseUser user = mAuth.getCurrentUser();
                 mLayoutManager = new LinearLayoutManager(TripDetails.this);
                 mRecyclerView.setLayoutManager(mLayoutManager);
                 mAdapter = new UserAdapter(usersList, mRecyclerView);
                 mRecyclerView.setAdapter(mAdapter);
                 if(user.getEmail().equals(objTrip.getCreatedBy()) || objTrip.getUsers().contains(user.getEmail())){
                     join_Trip.setVisibility(View.INVISIBLE);
                     delete_Trip.setVisibility(View.VISIBLE);
                     chat_button.setVisibility(View.VISIBLE);
                 }
                 else
                 {
                     join_Trip.setVisibility(View.VISIBLE);
                     delete_Trip.setVisibility(View.INVISIBLE);
                     chat_button.setVisibility(View.INVISIBLE);
                 }

            }
        }

        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetails.this, ChatBoxMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TripId", objTrip.tripId);
                intent.putExtra("IdBundle", bundle);
                startActivity(intent);

            }
        });

        join_Trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                users.add(user.getEmail());
                DocumentReference washingtonRef = db.collection("trips").document(objTrip.tripId);
                washingtonRef
                        .update("users", users)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                usersList.add(user.getEmail());
                                Log.d("", "DocumentSnapshot successfully updated!");
                                join_Trip.setVisibility(View.INVISIBLE);
                                delete_Trip.setVisibility(View.VISIBLE);
                                chat_button.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(TripDetails.this);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mAdapter = new UserAdapter(usersList, mRecyclerView);
                                mRecyclerView.setAdapter(mAdapter);
                                Toast.makeText(TripDetails.this, "Trip Joined Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("", "Error updating document", e);
                                Toast.makeText(TripDetails.this, "Cannot able to add to trip"+ e, Toast.LENGTH_SHORT).show();
                                join_Trip.setVisibility(View.VISIBLE);
                                delete_Trip.setVisibility(View.INVISIBLE);
                                chat_button.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });


        delete_Trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (user.getEmail().equals(objTrip.getCreatedBy())) {
                    db.collection("trips").document(objTrip.getTripId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(TripDetails.this, "Successfully removed from Trip", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TripDetails.this, "Error removing from trip. Please try again", Toast.LENGTH_SHORT).show();
                                    Log.w("", "Error deleting trip", e);
                                }
                            });
                } else {
                    users.remove(user.getEmail());
                    db.collection("trips").document(objTrip.getTripId())
                            .update("users", users)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("", "Trip successfully deleted!");
                                    join_Trip.setVisibility(View.VISIBLE);
                                    delete_Trip.setVisibility(View.INVISIBLE);
                                    chat_button.setVisibility(View.INVISIBLE);
                                    usersList.remove(user.getEmail());
                                    mLayoutManager = new LinearLayoutManager(TripDetails.this);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                    mAdapter = new UserAdapter(usersList, mRecyclerView);
                                    mRecyclerView.setAdapter(mAdapter);
                                    Toast.makeText(TripDetails.this, "Removed from trip successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TripDetails.this, "Error removing from trip. Please try after some time", Toast.LENGTH_SHORT).show();
                                    Log.w("", "Error deleting trip", e);
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(TripDetails.this, MainActivity.class);
            startActivity(intent);
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
                Intent intent = new Intent(TripDetails.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

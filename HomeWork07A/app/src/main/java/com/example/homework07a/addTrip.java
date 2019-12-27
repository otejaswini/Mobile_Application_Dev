package com.example.homework07a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class addTrip extends AppCompatActivity {

    EditText addTitle, addLocation;
    Button search, addTripbutton, addUsersbutton;
    TextView addLatLog;
    ImageView addImage;
    Location objLocation;
    boolean checkedLocation=false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ArrayList<String> userIds = new ArrayList<>();
    ArrayList<String> remainingUsers;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> tripUsersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Create a Trip");
        addTitle=findViewById(R.id.addtripTitle);
        addLocation=findViewById(R.id.addlocation);
        search=findViewById(R.id.addSearch);
        addTripbutton=findViewById(R.id.addTrip);
        addLatLog=findViewById(R.id.addLatLong);
        addImage=findViewById(R.id.addImageview);
        addUsersbutton = findViewById(R.id.addUsersbutton);
        mRecyclerView=findViewById(R.id.rv_users);
        mAuth = FirebaseAuth.getInstance();
        addUsersbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remainingUsers = new ArrayList<>();
                remainingUsers.clear();
                remainingUsers = userIds;
                remainingUsers.remove(mAuth.getCurrentUser().getEmail());
                if(remainingUsers.size()!=0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(addTrip.this);
                    builder.setTitle("Select Users")
                            .setItems(remainingUsers.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    tripUsersList.add(remainingUsers.get(which));
                                    userIds.remove(remainingUsers.get(which));
                                    mLayoutManager = new LinearLayoutManager(addTrip.this);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                    mAdapter = new UserAdapter(tripUsersList, mRecyclerView);
                                    mRecyclerView.setAdapter(mAdapter);

                                }
                            });
                    builder.create();
                    builder.show();
                }
                else{
                    Toast.makeText(addTrip.this, "No users found to add", Toast.LENGTH_SHORT).show();
                }

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Location = addLocation.getText().toString();
                if (Location.length() != 0) {
                    String Url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?inputtype=textquery&fields=photos,formatted_address,name,geometry&key=AIzaSyAkU_HY5mNwpjqcHmd5Zei04afiy-Fpep4" + "&input=" + Location;
                    LocAsync async = new LocAsync(addTrip.this);
                    async.execute(Url);
                }
                else{
                    addLocation.setError("Please enter location to search");
                }
            }
        });

        addTripbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trip_title=addTitle.getText().toString();
                if(checkedLocation=true&& trip_title.length()!=0){
                    String Place = objLocation.place;
                    String imageUrl =objLocation.photoUrl;
                    String Latitude = objLocation.latitude;
                    String Longitude= objLocation.longitude;
                    FirebaseUser user = mAuth.getCurrentUser();

                    String tripId = db.collection("trips").document().getId();
                    final Trips tripObj = new Trips(Latitude, Longitude, Place, imageUrl, user.getEmail(),trip_title, tripUsersList, tripId);
                    Map<String, Object> userMap = tripObj.toUserMap();
                    db.collection("trips").document(tripId)
                            .set(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(addTrip.this, "Trip created successfully", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("tripData",tripObj);
                                        Intent intent = new Intent(addTrip.this, TripsActivity.class);
                                        intent.putExtra("tripBundle", bundle);
                                        setResult(addTrip.RESULT_OK, intent);
                                        finish();
                                    } else {
                                        Toast.makeText(addTrip.this, "Trip creation Unsuccessful", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else
                {
                    if(checkedLocation=false){
                        if(addLocation.getText().toString().length()!=0)
                        {
                            addLocation.setError("Please click on check to set Location");
                        }
                        else{
                            addLocation.setError("Please enter location and click check");
                        }
                    }

                    if(trip_title.length()==0){
                        addTitle.setError("Please enter title");
                    }
                }
            }
        });
    }
    public void setLocationDetails(Location obj){

        checkedLocation=true;
        objLocation=obj;
        addLatLog.setText("Latitude: " + obj.getLatitude()+"; Longitude: "+ obj.getLongitude()+"; place: "+obj.getPlace());
        Picasso.get().load(obj.getPhotoUrl()).into(addImage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(addTrip.this, MainActivity.class);
            startActivity(intent);
        }
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userIds.add(document.getId());
                            }
                        }
                    }
                });

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
                Intent intent = new Intent(addTrip.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}


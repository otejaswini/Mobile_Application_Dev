package com.example.homework07a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    EditText fname;
    EditText lname;
    RadioGroup edit_gender;
    RadioButton edit_male;
    RadioButton edit_female;
    Button update;
    TextView editemail;
    ImageView profileImage;
    FirebaseAuth mAuth;
    String gender = null;
    String imageUrl = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    Bitmap selectedImage;
    final static int GALLERY = 8;
    final static int CAMERA = 5;
    String FirstName;
    String LastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editemail = findViewById(R.id.email_edit);
        fname = findViewById(R.id.editFirstName);
        lname = findViewById(R.id.editLastName);
        edit_gender = findViewById(R.id.editRadioGroup);
        edit_male = findViewById(R.id.editmale);
        edit_female = findViewById(R.id.editfemale);
        update = findViewById(R.id.updateProfile);
        profileImage = findViewById(R.id.editImageView);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        editemail.setText(user.getEmail());

        Context ctx = getApplicationContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString("Key", "");
        Gson gson = new Gson();
        User userObj = gson.fromJson(jsonString, User.class);

        if (userObj != null) {
            fname.setText(userObj.firstName);
            lname.setText(userObj.lastName);
            if (userObj.gender.equals("Male")) {
                edit_male.setChecked(true);
                gender = "Male";
            }
            if (userObj.gender.equals("Female")) {
                edit_female.setChecked(true);
                gender = "Female";
            }
            Picasso.get().load(userObj.photoURL).into(profileImage);
            imageUrl = userObj.photoURL;
        } edit_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.editmale) {
                    gender = "Male";
                }
                if (checkedId == R.id.editfemale) {
                    gender = "Female";
                }
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirstName = fname.getText().toString();
                LastName = lname.getText().toString();

                if (selectedImage == null) {
                    final User userObj = new User(FirstName, LastName, imageUrl, gender);
                    Map<String, Object> userMap = userObj.toUserMap();
                    db.collection("users").document(user.getEmail())
                            .set(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Gson gson = new Gson();
                                        String jsonString = gson.toJson(userObj);
                                        Context ctx = getApplicationContext();
                                        SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();
                                        editor.putString("Key", jsonString);
                                        editor.commit();
                                        finish();

                                    } else {
                                        Toast.makeText(EditProfile.this, "User profile update UnSuccessful", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } else {
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    final StorageReference storageReference = firebaseStorage.getReference();
                    String ref = "images/Image" + System.currentTimeMillis() + ".png";
                    final StorageReference imageRepository = storageReference.child(ref);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] data = byteArrayOutputStream.toByteArray();
                    UploadTask uploadTask = imageRepository.putBytes(data);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("demo", "onFailure: " + e.getMessage());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("demo", "onSuccess: Image upload Successful");
                        }
                    });

                    Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return imageRepository.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.d("demo", "Image" + task.getResult());
                                imageUrl = task.getResult().toString();
                                final User userObj = new User(FirstName, LastName, imageUrl, gender);
                                Map<String, Object> userMap = userObj.toUserMap();
                                db.collection("users").document(user.getEmail())
                                        .set(userMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Gson gson = new Gson();
                                                    String jsonString = gson.toJson(userObj);
                                                    Context ctx = getApplicationContext();
                                                    SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.clear();
                                                    editor.commit();
                                                    editor.putString("Key", jsonString);
                                                    editor.commit();
                                                    finish();
                                                    Toast.makeText(EditProfile.this, "User profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(EditProfile.this, "User profile update UnSuccessful", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });
    }
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    profileImage.setImageBitmap(bitmap);
                    selectedImage = bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profileImage.setImageBitmap(thumbnail);
            selectedImage = thumbnail;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(EditProfile.this, MainActivity.class);
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
                Intent intent = new Intent(EditProfile.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

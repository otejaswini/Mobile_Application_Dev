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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {
    EditText firstName;
    EditText lastName;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;
    ImageView photo;
    EditText emailCreate;
    EditText passwordCreate;
    FirebaseAuth mAuth;
    final static int GALLERY = 8;
    final static int CAMERA = 5;
    String storage = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/homework-parta.appspot.com/o/user_image.png?alt=media&token=000e5673-a3bd-47a8-b11b-ec7cdb4779f4";
    String Gender = null;
    String photoUrl;
    Button createProfile;
    private static final String TAG = "SignUp";
    Bitmap selectedImage;
    User userObj;
    String email;
    String password;
    String FirstName;
    String LastName;
    ProgressBar pb_createProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Create Profile");
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        gender = findViewById(R.id.radioGroup);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        photo = findViewById(R.id.photo);
        emailCreate = findViewById(R.id.emailCreate);
        passwordCreate = findViewById(R.id.pwdCreate);
        createProfile = findViewById(R.id.createProfile);
        mAuth = FirebaseAuth.getInstance();
        pb_createProfile= findViewById(R.id.pb_createProfile);
        pb_createProfile.setVisibility(View.INVISIBLE);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    Gender = "Male";
                }
                if (checkedId == R.id.female) {
                    Gender = "Female";
                }
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailCreate.getText().toString();
                password = passwordCreate.getText().toString();
                FirstName = firstName.getText().toString();
                LastName = lastName.getText().toString();
                if (email.length() != 0 && password.length() >=6 && firstName.length() != 0 & gender != null) {
                    pb_createProfile.setVisibility(View.VISIBLE);
                    if (selectedImage == null) {
                        String emailStr = emailCreate.getText().toString();
                        final User userObj = new User(FirstName, LastName, defaultImageUrl, Gender);
                        Map<String, Object> userMap = userObj.toUserMap();
                        db.collection("users").document(emailStr)
                                .set(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mAuth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(CreateProfile.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                FirebaseUser user = mAuth.getCurrentUser();
                                                                Toast.makeText(CreateProfile.this, "New User Created",
                                                                        Toast.LENGTH_SHORT).show();
                                                                Gson gson = new Gson();
                                                                String jsonString = gson.toJson(userObj);
                                                                Context ctx = getApplicationContext();
                                                                SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                editor.putString("Key", jsonString);
                                                                editor.commit();
                                                                pb_createProfile.setVisibility(View.INVISIBLE);
                                                                Intent intent = new Intent(CreateProfile.this, TripsActivity.class);
                                                                startActivity(intent);


                                                            } else {
                                                                pb_createProfile.setVisibility(View.INVISIBLE);
                                                                Log.w("", "createUserWithEmail:failure", task.getException());
                                                                Toast.makeText(CreateProfile.this, "Failed to create user"+task.getException(),
                                                                        Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });
                                        } else {
                                            pb_createProfile.setVisibility(View.INVISIBLE);
                                            Toast.makeText(CreateProfile.this, "User profile creation UnSuccessful", Toast.LENGTH_SHORT).show();

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
                                    photoUrl = task.getResult().toString();
                                    final User userObj = new User(FirstName, LastName, photoUrl, Gender);
                                    Map<String, Object> userMap = userObj.toUserMap();
                                    String emailStr = emailCreate.getText().toString();
                                    db.collection("users").document(emailStr)
                                            .set(userMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                                .addOnCompleteListener(CreateProfile.this, new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        if (task.isSuccessful()) {
                                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                                            Toast.makeText(CreateProfile.this, "New User Created",
                                                                                    Toast.LENGTH_SHORT).show();
                                                                            Gson gson = new Gson();
                                                                            String jsonString = gson.toJson(userObj);
                                                                            Context ctx = getApplicationContext();
                                                                            SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
                                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                            editor.putString("Key", jsonString);
                                                                            editor.commit();
                                                                            pb_createProfile.setVisibility(View.INVISIBLE);
                                                                            Intent intent = new Intent(CreateProfile.this, TripsActivity.class);
                                                                            startActivity(intent);

                                                                        } else {
                                                                            pb_createProfile.setVisibility(View.INVISIBLE);
                                                                            Log.w("", "createUserWithEmail:failure", task.getException());
                                                                            Toast.makeText(CreateProfile.this, "Failed to create user"+task.getException(),
                                                                                    Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        pb_createProfile.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(CreateProfile.this, "User profile creation UnSuccessful", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                            }
                        });
                    }
                }
                else{
                    if(email.length()==0)
                    {
                        emailCreate.setError("Please enter email");
                    }
                    if(password.length()==0)
                    {
                        passwordCreate.setError("Please enter password");
                    }
                    if(password.length()>0 &&password.length()<6)
                    {
                        firstName.setError("Password must be at least six characters");
                    }

                    if(firstName.length()==0){
                        lastName.setError("Please enter First Name");
                    }

                    if(gender==null){
                        Toast.makeText(CreateProfile.this, "Please select gender", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 0);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    photo.setImageBitmap(bitmap);
                    selectedImage = bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(thumbnail);
            selectedImage = thumbnail;
        }
    }
}
package com.lemon.check.checkesandchess.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.tools.CropImageActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView profileImageView;
    EditText inputUsername, inputiCity, inputCoutry, inputProfessionn;
    Button btnUpdate;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    StorageReference StorageRef;

    String userId;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE_CROP = 102;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = getIntent().getStringExtra("userId");

        profileImageView = findViewById(R.id.profileImageView);
        inputUsername = findViewById(R.id.inputUsername);
        inputiCity = findViewById(R.id.inputiCity);
        inputCoutry = findViewById(R.id.inputCoutry);
        inputProfessionn = findViewById(R.id.inputProfessionn);
        btnUpdate = findViewById(R.id.btnUpdate);

        getSupportActionBar().setTitle("Edit Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        StorageRef = FirebaseStorage.getInstance().getReference().child("profileImages");
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrI = snapshot.child("profileImage").getValue().toString();
                    String city = snapshot.child("city").getValue().toString();
                    String country = snapshot.child("country").getValue().toString();
                    String profession = snapshot.child("profession").getValue().toString();
                    String username = snapshot.child("names").getValue().toString();

                    Picasso.get().load(profileImageUrI).into(profileImageView);
                    inputiCity.setText(city);
                    inputUsername.setText(username);
                    inputCoutry.setText(country);
                    inputProfessionn.setText(profession);
                } else {
                    Toast.makeText(ProfileActivity.this, "Data does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, " " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Intent cropIntent = new Intent(this, CropImageActivity.class);
                cropIntent.putExtra("imageUri", selectedImageUri.toString());
                startActivityForResult(cropIntent, REQUEST_CODE_CROP);
            }
        } else if (requestCode == REQUEST_CODE_CROP && resultCode == RESULT_OK && data != null) {
            Uri croppedImageUri = data.getParcelableExtra("resultUri");
            if (croppedImageUri != null) {
                mImageUri = croppedImageUri;
                Picasso.get().load(mImageUri).into(profileImageView);
            }
        }
    }

    private void updateProfile() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Profile...");
        progressDialog.show();

        String username = inputUsername.getText().toString().trim();
        String city = inputiCity.getText().toString().trim();
        String country = inputCoutry.getText().toString().trim();
        String profession = inputProfessionn.getText().toString().trim();

        mUserRef.child("names").setValue(username);
        mUserRef.child("city").setValue(city);
        mUserRef.child("country").setValue(country);
        mUserRef.child("profession").setValue(profession);

        if (mImageUri != null) {
            uploadImage(progressDialog);
        } else {
            Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();
        }
    }

    private void uploadImage(ProgressDialog progressDialog) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("profile_images/" + user.getUid() + ".jpg");
        storageRef.putFile(mImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUri = uriTask.getResult();
                    String imageUrl = downloadUri.toString();
                    mUserRef.child("profileImage").setValue(imageUrl)
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

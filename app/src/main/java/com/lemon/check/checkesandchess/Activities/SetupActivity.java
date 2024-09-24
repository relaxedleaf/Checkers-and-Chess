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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lemon.check.checkesandchess.MainActivity;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.tools.CropImageActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_CROP = 102;

    CircleImageView profileImageView;
    EditText inputName, inputGender, inputCity, inputCountry, inputProfession;
    Button buttonSave;
    Uri imageUri;
    ///Firebase
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mRef;
    StorageReference StorageRef;
    ProgressDialog mLoadingBar;
    long coins = 0;
    String status;

    String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        profileImageView = findViewById(R.id.profile_image);
        inputGender = findViewById(R.id.inputGender);
        inputName = findViewById(R.id.inputName);
        inputCity = findViewById(R.id.inputCity);
        inputCountry = findViewById(R.id.inputCountry);
        inputProfession = findViewById(R.id.inputProfession);
        buttonSave = findViewById(R.id.buttonSave);
        mLoadingBar = new ProgressDialog(this);

        getSupportActionBar().setTitle("Profile Setup");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        StorageRef = FirebaseStorage.getInstance().getReference().child("profileImages");

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
    }

    private void SaveData() {
        String names = inputName.getText().toString();
        String gender = inputGender.getText().toString();
        String city = inputCity.getText().toString();
        String country = inputCountry.getText().toString();
        String profession = inputProfession.getText().toString();
        if (gender.isEmpty() || gender.length() < 3) {
            showError(inputGender, "Gender is not valid");
        } else if (names.isEmpty() || names.length() < 3) {
            showError(inputName, "Name is not valid");
        } else if (city.isEmpty() || city.length() < 3) {
            showError(inputCity, "City is not valid");
        } else if (country.isEmpty() || country.length() < 3) {
            showError(inputCountry, "Country is not valid");
        } else if (profession.isEmpty() || profession.length() < 3) {
            showError(inputProfession, "Profession is not valid");
        } else if (imageUri == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        } else {
            mLoadingBar.setTitle("Adding Setup Profile");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            StorageRef.child(user.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        StorageRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("names", names);
                                hashMap.put("gender", gender);
                                hashMap.put("city", city);
                                hashMap.put("country", country);
                                hashMap.put("profession", profession);
                                hashMap.put("profileImage", uri.toString());
                                hashMap.put("coins", coins);
                                hashMap.put("uid", uid);

                                mRef.child(user.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(SetupActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        mLoadingBar.dismiss();
                                        Toast.makeText(SetupActivity.this, "Setup Profile Completed", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(SetupActivity.this, "Verify your Email and Sign in", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mLoadingBar.dismiss();
                                        Toast.makeText(SetupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Intent cropIntent = new Intent(this, CropImageActivity.class);
                cropIntent.putExtra("imageUri", selectedImageUri.toString());
                startActivityForResult(cropIntent, REQUEST_CODE_CROP);
            }
        } else if (requestCode == REQUEST_CODE_CROP && resultCode == RESULT_OK && data != null) {
            Uri croppedImageUri = data.getParcelableExtra("resultUri");
            if (croppedImageUri != null) {
                imageUri = croppedImageUri;
                profileImageView.setImageURI(imageUri);
            }
        }
    }
}

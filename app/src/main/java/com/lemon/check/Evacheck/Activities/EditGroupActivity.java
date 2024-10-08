package com.lemon.check.Evacheck.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lemon.check.Evacheck.R;
import com.lemon.check.Evacheck.tools.CropImageActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditGroupActivity extends AppCompatActivity {
    private CircleImageView GrpProfl_Img;
    private EditText GrpTitle, GrpDescrip;
    private Button btnUpdate;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE_CROP = 102; // Define a request code for cropping
    private Uri mImageUri; // Uri to store the selected image URI
    private ProgressDialog progressDialog; // Progress dialog for image upload

    private String originalGroupTitle; // Store the original group title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        GrpProfl_Img = findViewById(R.id.groupImageView);
        GrpTitle = findViewById(R.id.GROUP_Title);
        GrpDescrip = findViewById(R.id.Group_Desc);
        btnUpdate = findViewById(R.id.btnUpdata);

        String groupId = getIntent().getStringExtra("groupId");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false); // Prevent dismiss on touch outside

        getSupportActionBar().setTitle("Edit Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fetch and display group information
        fetchGroupInformation(groupId);

        GrpProfl_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(); // Trigger image selection process
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String updatedGroupTitle = GrpTitle.getText().toString();
                final String updatedGroupDescription = GrpDescrip.getText().toString();

                // Check if the title has changed
                if (!updatedGroupTitle.equals(originalGroupTitle)) {
                    // Check for duplicate title
                    DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference("Groups");
                    groupsRef.orderByChild("groupTitle").equalTo(updatedGroupTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Group with the same title already exists
                                Toast.makeText(EditGroupActivity.this, "A group with the same title already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                // No group with the same title exists, proceed with updating the group
                                updateGroupInformation(groupId, updatedGroupTitle, updatedGroupDescription);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                            progressDialog.dismiss();
                            Toast.makeText(EditGroupActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Title has not changed, update group information directly
                    updateGroupInformation(groupId, updatedGroupTitle, updatedGroupDescription);
                }
            }
        });
    }

    private void fetchGroupInformation(String groupId) {
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    originalGroupTitle = dataSnapshot.child("groupTitle").getValue(String.class); // Store original title
                    String groupDescription = dataSnapshot.child("groupDescription").getValue(String.class);
                    String groupIconUrl = dataSnapshot.child("groupIcon").getValue(String.class);

                    // Display fetched data in the UI
                    GrpTitle.setText(originalGroupTitle);
                    GrpDescrip.setText(groupDescription);

                    // Load group icon using Glide or any other image loading library
                    Glide.with(EditGroupActivity.this)
                            .load(groupIconUrl)
                            .into(GrpProfl_Img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Group Icon"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
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
                GrpProfl_Img.setImageURI(mImageUri); // Display cropped image in ImageView
            }
        }
    }

    private void updateGroupInformation(String groupId, String updatedGroupTitle, String updatedGroupDescription) {
        progressDialog.show();

        // Update the group information in Firebase
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.child("groupTitle").setValue(updatedGroupTitle);
        groupRef.child("groupDescription").setValue(updatedGroupDescription);

        // Update group icon if a new image is selected
        if (mImageUri != null) {
            uploadImage(groupId);
        } else {
            // Show a toast message and navigate back to the previous activity
            Toast.makeText(EditGroupActivity.this, "Group information updated successfully.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();
        }
    }

    private void uploadImage(final String groupId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("group_icons/" + groupId);
        storageRef.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss(); // Hide the progress dialog
                        Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
                                groupRef.child("groupIcon").setValue(imageUrl);

                                Toast.makeText(EditGroupActivity.this, "Group information updated successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss(); // Hide the progress dialog
                        Toast.makeText(EditGroupActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

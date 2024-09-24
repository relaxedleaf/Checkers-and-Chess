package com.lemon.check.checkesandchess.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.R;

public class GroupInfoActivity extends AppCompatActivity {

    private ImageView GrpProfl_Img;
    private TextView GrpTitle, CreatedBy, GrpDescrip, editGroupTv;
    private String groupId;
    Button leave_group;
    Button admob_btn;
    private Button shareGroupBtn_link, participantsBtn ;
    private String createdByUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        getSupportActionBar().setTitle("Group Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GrpProfl_Img = findViewById(R.id.groupIconIv);
        GrpTitle = findViewById(R.id.grpTitle);
        CreatedBy = findViewById(R.id.createdByTv);
        GrpDescrip = findViewById(R.id.descriptionTv);
        editGroupTv = findViewById(R.id.editGroupTv);
        leave_group = findViewById(R.id.leave_group);
        shareGroupBtn_link = findViewById(R.id.shareGroupBtn_link);
        participantsBtn = findViewById(R.id.participantsBtn);
        admob_btn = findViewById(R.id.admob_btn);


        // Get the group ID from the intent or any other source
        Intent intent = getIntent();
        groupId = getIntent().getStringExtra("groupId");

        // Fetch and display group information
        fetchGroupInformation(groupId);
        // Check if the current user is the creator of the group
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(createdByUserId)) {
            admob_btn.setVisibility(View.VISIBLE);
            admob_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupInfoActivity.this, EnterPaymentDetailsActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            admob_btn.setVisibility(View.GONE);
        }

        participantsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the participants activity
                Intent intent = new Intent(GroupInfoActivity.this, GroupParticipants.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });

        shareGroupBtn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Share group link logic
                shareGroupLink();

            }
        });
        leave_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the current user is the creator of the group
                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(createdByUserId)) {
                    // Show a toast message indicating that the creator cannot leave the group
                    Toast.makeText(GroupInfoActivity.this, "Group creator cannot leave the group.", Toast.LENGTH_SHORT).show();
                } else {
                    // Remove the current user from the group participants list
                    DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("Participants");
                    participantsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    Toast.makeText(GroupInfoActivity.this, "You have left the group.", Toast.LENGTH_SHORT).show();
                    // Navigate back to the previous activity
                    finish();
                }
            }
        });

        editGroupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the current user is the creator of the group
                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(createdByUserId)) {
                    // Handle edit/delete operations
                    editOrDeleteGroup();
                } else {
                    // Show message that only the creator can edit/delete the group
                    Toast.makeText(GroupInfoActivity.this, "Only the group creator can edit or delete the group.", Toast.LENGTH_SHORT).show();
                }
            }
        });
       // return false;
    }

    private void shareGroupLink() {
        // Generate a unique link for the group
        String groupLink = "https://lemonvalley.com/join_group/" + groupId;
        // Create an intent to share the link
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Join our group on MyApp: " + groupLink);
        // Start the share intent
        startActivity(Intent.createChooser(shareIntent, "Share Group Link"));


    }

    private void fetchGroupInformation(String groupId) {
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);

        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String groupTitle = dataSnapshot.child("groupTitle").getValue(String.class);
                    createdByUserId = dataSnapshot.child("createdBy").getValue(String.class);
                    String groupDescription = dataSnapshot.child("groupDescription").getValue(String.class);
                    String groupIconUrl = dataSnapshot.child("groupIcon").getValue(String.class);
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    // Check if the current user is the creator
                    if (currentUserId.equals(createdByUserId)) {
                        // User is the creator, allow access to the AdMob button
                        admob_btn.setVisibility(View.VISIBLE);
                        //setupAdMobButton();
                    } else {
                        // User is not the creator, hide the AdMob button
                        admob_btn.setVisibility(View.INVISIBLE); // Set to VISIBLE for testing purposes
                        // admobdBtn.setVisibility(View.GONE);
                    }

                    // Display fetched data in the UI
                    GrpTitle.setText(groupTitle);
                    GrpDescrip.setText(groupDescription);

                    // Load group icon using Glide or any other image loading library
                    Glide.with(GroupInfoActivity.this)
                            .load(groupIconUrl)
                            .into(GrpProfl_Img);

                    // Fetch and display the creator's username
                    fetchCreatorUsername(createdByUserId);
                }
                //return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void fetchCreatorUsername(String createdByUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(createdByUserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String creatorUsername = dataSnapshot.child("names").getValue(String.class);

                    // Display creator's username in the UI
                    CreatedBy.setText("Created by: " + creatorUsername);
                }
               //return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void editOrDeleteGroup() {
        // Check if the current user is the creator of the group
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(createdByUserId)) {
            // Show a dialog with options to edit or delete the group
            AlertDialog.Builder builder = new AlertDialog.Builder(GroupInfoActivity.this);
            builder.setTitle("Edit or Delete Group");
            builder.setItems(new String[]{"Edit Group", "Delete Group"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            // Edit group
                            editGroup();
                            break;
                        case 1:
                            // Delete group
                            deleteGroup();
                            break;
                    }
                }
            });
            builder.show();
        } else {
            // Show a toast message that only the creator can edit/delete the group
            Toast.makeText(GroupInfoActivity.this, "Only the group creator can edit or delete the group.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editGroup() {
        // Navigate to a new activity to edit the group information
        Intent intent = new Intent(GroupInfoActivity.this, EditGroupActivity.class);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }

    private void deleteGroup() {
        // Delete the group from the Firebase Realtime Database
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.removeValue();
        Toast.makeText(this, "Group Deleted.....", Toast.LENGTH_SHORT).show();
        // Navigate back to the previous activity
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
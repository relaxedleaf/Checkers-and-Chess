
package com.lemon.check.checkesandchess.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.Utils.Participant;
import com.lemon.check.checkesandchess.adapters.GroupParticipantsAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupParticipants extends AppCompatActivity implements GroupParticipantsAdapter.OnRemoveParticipantListener {
    private RecyclerView recyclerView;
    private CircleImageView userProfilecreator;
    private TextView usernameAppbarcreator, Grp_members_counter;
    private Button removeParticipantButton;
    private GroupParticipantsAdapter adapter;
    private List<Participant> participantList;
    private String groupId;
    private String groupCreatorId;
    private String createdByUserId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_participants);

        getSupportActionBar().setTitle("Group Participants");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Initialize views
        userProfilecreator = findViewById(R.id.userProfilecreator);
        usernameAppbarcreator = findViewById(R.id.usernameAppbarcreator);
        removeParticipantButton = findViewById(R.id.removeButton);
        Grp_members_counter = findViewById(R.id.Grp_members_counter);
        recyclerView = findViewById(R.id.recycler_view_group_participants);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get data from intent
        groupId = getIntent().getStringExtra("groupId");
        createdByUserId = getIntent().getStringExtra("createdByUserId");

        // Initialize participant list
        participantList = new ArrayList<>();

        // Initialize adapter
        adapter = new GroupParticipantsAdapter(participantList, this);
        recyclerView.setAdapter(adapter);

        // Fetch and display group information
        fetchGroupInformation(groupId);

        // Fetch and display participants
        fetchParticipants();
       // return false;
    }

    private void fetchGroupInformation(String groupId) {
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);

        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String groupTitle = dataSnapshot.child("groupTitle").getValue(String.class);
                    createdByUserId = dataSnapshot.child("createdBy").getValue(String.class);
                    String groupDescription = dataSnapshot.child("groupDescription").getValue(String.class);
                    String groupIconUrl = dataSnapshot.child("groupIcon").getValue(String.class);

                    // Fetch and display the creator's username
                    fetchCreatorUsername(createdByUserId);
                    // Call this method in the onCreate() method after fetching group information
                    fetchGroupCreatorPicture(createdByUserId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void fetchGroupCreatorPicture(String createdByUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(createdByUserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileImage = dataSnapshot.child("profileImage").getValue(String.class);

                    // Load the creator's profile picture using Glide or any other image loading library
                    Glide.with(GroupParticipants.this)
                            .load(profileImage)
                            .into(userProfilecreator);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private void fetchCreatorUsername(String createdByUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(createdByUserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String creatorUsername = dataSnapshot.child("names").getValue(String.class);

                    // Display creator's username in the UI
                    usernameAppbarcreator.setText("Group Admin: " + creatorUsername);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });

    }
    private void fetchParticipants() {
        DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference("Groups")
                .child(groupId).child("Participants");

        participantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    participantList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String participantId = snapshot.getKey();
                        // Fetch participant details and createdByUserId
                        fetchParticipantDetails(participantId);
                        // Update the TextView with the number of participants
                        int participantsCount = (int) dataSnapshot.getChildrenCount();
                        Grp_members_counter.setText("Participants: " + participantsCount);


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void fetchParticipantDetails(String participantId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(participantId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String names = dataSnapshot.child("names").getValue(String.class);
                    String profileImage = dataSnapshot.child("profileImage").getValue(String.class);
                    String uid = dataSnapshot.getKey();
                    // Create GroupParticipant object and add to list
                    String createdBy = getIntent().getStringExtra("createdByUserId");
                    String Participant = dataSnapshot.child("Participant").getValue(String.class);
                    Participant participant  = new Participant(names, profileImage,uid,createdBy,Participant);
                    participantList.add(participant);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
        //........
        // Fetch group creator ID
        DatabaseReference  groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    groupCreatorId = snapshot.child("createdBy").getValue(String.class);
                    // Remove the creator from the participant list
                    for (int i = 0; i < participantList.size(); i++) {
                        if (participantList.get(i).getUid().equals(groupCreatorId)) {
                            participantList.remove(i);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });

    }
    @Override
    public void onRemoveParticipant(Participant participant) {
        // Check if the current user is the creator of the group
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            if (currentUserId.equals(groupCreatorId)) {
                // Check if the participant being removed is not the creator
                if (!participant.getUid().equals(currentUserId)) {
                    // Remove the participant
                    DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference("Groups")
                            .child(groupId).child("Participants").child(participant.getUid());
                    participantsRef.removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(GroupParticipants.this, "Participant removed successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(GroupParticipants.this, "Failed to remove participant", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // Display message indicating the creator cannot remove themselves
                    Toast.makeText(GroupParticipants.this, "You cannot remove yourself as the creator", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Display message indicating only the creator can remove participants
                Toast.makeText(GroupParticipants.this, "Only the creator can remove participants", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

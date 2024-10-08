package com.lemon.check.Evacheck.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.lemon.check.Evacheck.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewGroupActivity extends AppCompatActivity {

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    long coins=0;
    FirebaseDatabase database;
    String currentUid;

    CircleImageView GrpProfl_Img;
    TextView GrpTitle, CreatedBy, GrpDescrip, Group_members;
    Button btnFollow_group;
    private String groupId;
    private String userRole; // Variable to store the user's role in the group
    String Participants;

    private RewardedInterstitialAd mRewardedInterstitialAd;
    private boolean adIsLoading;
    private boolean adShowing; // Flag to track if an ad is currently showing
    private long lastAdShownTime; // To track the last ad shown time
    private static final long AD_COOLDOWN_TIME = 300000; // 5 minutes cooldown
    private static final long MIN_INTERACTION_TIME = 60000; // 1 minute minimum interaction time
    private static final int MAX_ADS_PER_SESSION = 3; // Maximum ads per session
    private int adsShownThisSession = 0; // Counter for ads shown in the current session
    private Handler handler = new Handler(); // Handler for delaying ad loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
        GrpProfl_Img = findViewById(R.id.GrpProfl_Img);
        GrpTitle = findViewById(R.id.GrpTitle);
        CreatedBy = findViewById(R.id.CreatedBy);
        GrpDescrip = findViewById(R.id.GrpDescrip);
        btnFollow_group = findViewById(R.id.btnFollow_group);
        Group_members = findViewById(R.id.Group_members);

        getSupportActionBar().setTitle("Group Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        currentUid = FirebaseAuth.getInstance().getUid();

        if (user != null) {
            database.getReference().child("Users")
                    .child(currentUid)
                    .child("coins")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            coins = snapshot.getValue(Integer.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                 }

        // Get the group ID from the intent or any other source
        groupId = getIntent().getStringExtra("groupId");

        // Fetch and display group information
        fetchGroupInformation(groupId);





        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Create a reference to the group in the database
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.child("Participants").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User is a participant, hide the button
                    btnFollow_group.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });

        // Set click listener for the "Follow" button
        btnFollow_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeUserParticipant();
            }
        });

        // Delay the first ad load to ensure some interaction time
        handler.postDelayed(this::loadAd, MIN_INTERACTION_TIME);
    }

    private void fetchGroupInformation(String groupId) {
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);

        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String groupTitle = dataSnapshot.child("groupTitle").getValue().toString();
                    String createdByUserId = dataSnapshot.child("createdBy").getValue().toString();
                    String groupDescription = dataSnapshot.child("groupDescription").getValue().toString();
                    String groupIconUrl = dataSnapshot.child("groupIcon").getValue().toString();

                    // Display fetched data in the UI
                    GrpTitle.setText(groupTitle);
                    GrpDescrip.setText(groupDescription);
                    Picasso.get().load(groupIconUrl).into(GrpProfl_Img);

                    // Fetch and display the number of participants
                    fetchGroupParticipants(groupId);

                    // Fetch and display the creator's username
                    fetchCreatorUsername(createdByUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void fetchGroupParticipants(String groupId) {
        DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("Participants");

        participantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int participantsCount = (int) dataSnapshot.getChildrenCount();
                // Update the TextView with the number of participants
                Group_members.setText("Participants: " + participantsCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void makeUserParticipant() {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Create a reference to the group in the database
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);

        // Add the current user as a participant
        groupRef.child("Participants").child(currentUser.getUid()).setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Hide the "Follow" button
                        btnFollow_group.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle error if adding participant fails
                    }
                });
    }

    private void fetchCreatorUsername(String createdByUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(createdByUserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String creatorUsername = dataSnapshot.child("names").getValue().toString();
                    CreatedBy.setText("Created by: " + creatorUsername);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void loadAd() {
        if (adIsLoading || adShowing || adsShownThisSession >= MAX_ADS_PER_SESSION) {
            return;
        }

        adIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(this, "ca-app-pub-1134102830016252/8755790828", adRequest,
                new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                        mRewardedInterstitialAd = rewardedInterstitialAd;
                        adIsLoading = false;
                        showAd();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedInterstitialAd = null;
                        adIsLoading = false;
                        Toast.makeText(ViewGroupActivity.this, "Ad failed to load", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAd() {
        if (mRewardedInterstitialAd != null && !adShowing && System.currentTimeMillis() - lastAdShownTime >= AD_COOLDOWN_TIME) {
            adShowing = true;
            mRewardedInterstitialAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    int rewardAmount = rewardItem.getAmount();
                    coins += rewardAmount;
                    mUserRef.child(user.getUid()).child("coins").setValue(coins).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ViewGroupActivity.this, "Reward earned: " + rewardAmount + " coins", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ViewGroupActivity.this, "Failed to update coins", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    adShowing = false;
                    lastAdShownTime = System.currentTimeMillis();
                    adsShownThisSession++;
                    // Delay the next ad load to ensure some interaction time
                    handler.postDelayed(ViewGroupActivity.this::loadAd, MIN_INTERACTION_TIME);
                }
            });
        } else if (mRewardedInterstitialAd == null) {
            Toast.makeText(this, "The rewarded interstitial ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
            adShowing = false;
            loadAd();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Resume or start ad loading when the activity starts
        if (!adIsLoading && mRewardedInterstitialAd == null) {
            loadAd();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mRewardedInterstitialAd != null && !adShowing && adsShownThisSession < MAX_ADS_PER_SESSION) {
            showAd();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause ad loading if needed
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Cleanup or additional logic when the activity stops
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cleanup ad references when the activity is destroyed
        mRewardedInterstitialAd = null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

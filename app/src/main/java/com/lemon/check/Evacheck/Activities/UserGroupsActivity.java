package com.lemon.check.Evacheck.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.Evacheck.R;
import com.lemon.check.Evacheck.adapters.UserGroupsAdapter;
import com.lemon.check.Evacheck.models.GroupModel;

import java.util.ArrayList;
import java.util.List;

public class UserGroupsActivity extends AppCompatActivity {


    private RecyclerView recyclerViewUserGroups;
    private UserGroupsAdapter userGroupsAdapter;
    private List<GroupModel> userGroupsList;

    FirebaseUser mUser;

    private FirebaseUser user;
    DatabaseReference mUserRef;
    long coins = 0;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_groups);

        recyclerViewUserGroups = findViewById(R.id.recyclerViewUserGroups);
        recyclerViewUserGroups.setLayoutManager(new LinearLayoutManager(this));

        userGroupsList = new ArrayList<>();
        userGroupsAdapter = new UserGroupsAdapter(userGroupsList);

        recyclerViewUserGroups.setAdapter(userGroupsAdapter);
        ////here

        // Inside UserGroupsActivity
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

       checkCoinsAndInitialize();
    }

    private void checkCoinsAndInitialize() {
        if (user != null) {
            mUserRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Long coinsValue = snapshot.child("coins").getValue(Long.class);
                        if (coinsValue != null) {
                            coins = coinsValue;
                            if (coins >= 5) {
                                initializeRecyclerView();
                            } else {
                                Toast.makeText(UserGroupsActivity.this, "Insufficient Coins Watch Reward Ads To Earn Coins!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Log.e("ChatUsersActivity", "Coins value is null");
                            finish();
                        }
                    } else {
                        Log.e("ChatUsersActivity", "User snapshot does not exist");
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ChatUsersActivity", "Database error: " + error.getMessage());
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    private void initializeRecyclerView() {
        recyclerViewUserGroups.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    View view = rv.findChildViewUnder(e.getX(), e.getY());
                    if (view != null) {
                        int position = rv.getChildAdapterPosition(view);

                        // Get the clicked group
                        GroupModel clickedGroup = userGroupsList.get(position);

                        // Start the GroupChat activity with the group ID
                        Intent intent = new Intent(UserGroupsActivity.this, GroupChat.class);
                        intent.putExtra("groupId", clickedGroup.getGroupId());
                        startActivity(intent);
                        // Deduct coins only when the RecyclerView is initialized (activity is allowed to be opened)
                        deductCoins(5);
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        /////here

        fetchUserGroups();
        // return false;
    }

    private void fetchUserGroups() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference("Groups");

            groupsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userGroupsList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("Participants").child(currentUserId).exists()) {
                            // User is a participant in this group
                            GroupModel group = snapshot.getValue(GroupModel.class);
                            userGroupsList.add(group);
                        } else if (snapshot.child("creator").child(currentUserId).exists()) {
                            // User is a participant in this group
                            GroupModel group = snapshot.getValue(GroupModel.class);
                            userGroupsList.add(group);
                        }

                        userGroupsAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void deductCoins(long amount) {
        coins -= amount;
        mUserRef.child(user.getUid()).child("coins").setValue(coins);
    }
    public void increaseCoins(long amount) {
        coins += amount;
        mUserRef.child(user.getUid()).child("coins").setValue(coins);
    }
}
//}
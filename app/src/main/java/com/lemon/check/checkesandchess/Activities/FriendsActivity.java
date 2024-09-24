package com.lemon.check.checkesandchess.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.Utils.Friends;
import com.squareup.picasso.Picasso;

public class FriendsActivity extends AppCompatActivity {
    FirebaseRecyclerOptions<Friends>options;
    FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>adapter;
    RecyclerView recyclerviewfriends;
    FirebaseAuth mAuth;
    DatabaseReference mref,requestsRef;
    private static final int REQUEST_CODE_CHAT = 1;
    private static final int CHAT_ACTIVITY_REQUEST = 2;

    FirebaseUser mUser;

    private FirebaseUser user;
    DatabaseReference mUserRef;
    long coins = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerviewfriends=findViewById(R.id.recyclerviewfriends);
        recyclerviewfriends.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference().child("Friends");

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
                                Toast.makeText(FriendsActivity.this, "Insufficient Coins Watch Reward Ads To Earn Coins!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Log.e("FriendsActivity", "Coins value is null");
                            finish();
                        }
                    } else {
                        Log.e("FriendsActivity", "User snapshot does not exist");
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FriendsActivity", "Database error: " + error.getMessage());
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    private void initializeRecyclerView() {
        Query query = mref.child(mUser.getUid()).orderByChild("names").startAt("").endAt("\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query, Friends.class).build();
        adapter = new FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendMyViewHolder holder, int i, @NonNull Friends model) {
                loadOtherUser(getRef(i).getKey(), holder);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                        intent.putExtra("otherUserId", getRef(i).getKey()).toString();
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_friend, parent, false);
                return new FriendMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerviewfriends.setAdapter(adapter);

        // Deduct coins only when the RecyclerView is initialized (activity is allowed to be opened)
        deductCoins(5);
    }

    private void loadOtherUser(String otherUserId, @NonNull FriendMyViewHolder holder) {
        mUserRef.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String otherUsername = snapshot.child("names").getValue(String.class);
                    String otherUserProfileImageLink = snapshot.child("profileImage").getValue(String.class);

                    fetchMessageCount(otherUserId, holder.textView_message_count);

                    holder.username.setText(otherUsername);
                    holder.profession.setText(snapshot.child("profession").getValue(String.class));
                    Picasso.get().load(otherUserProfileImageLink).into(holder.profileImageUrI);
                } else {
                    Log.e("FriendsActivity", "User snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FriendsActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deductCoins(long amount) {
        coins -= amount;
        mUserRef.child(user.getUid()).child("coins").setValue(coins);
    }

    private void fetchMessageCount(String otherUserId, TextView textViewMessageCount) {
        DatabaseReference messageCountRef = FirebaseDatabase.getInstance().getReference().child("MessageCount")
                .child(mUser.getUid()).child(otherUserId);

        messageCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long messageCount = 0;
                if (dataSnapshot.exists()) {
                    messageCount = dataSnapshot.getValue(Long.class);
                }
                textViewMessageCount.setText(String.valueOf(messageCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FriendsActivity", "Failed to fetch message count: " + databaseError.getMessage());
                textViewMessageCount.setText("0");
            }
        });
    }
    public void increaseCoins(long amount) {
        coins += amount;
        mUserRef.child(user.getUid()).child("coins").setValue(coins);
    }

}

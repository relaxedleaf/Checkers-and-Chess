package com.lemon.check.checkesandchess.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.Utils.Users;
import com.squareup.picasso.Picasso;

public class FindFriendActivity extends AppCompatActivity {
    FirebaseRecyclerOptions<Users> options;
    FirebaseRecyclerAdapter<Users, FindFriendViewHold> adapter;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    long coins=0;
    FirebaseDatabase database;
    String currentUid;
    RecyclerView recyclerViewB;

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
        setContentView(R.layout.activity_find_friend);

        getSupportActionBar().setTitle("Find Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewB = findViewById(R.id.recyclerViewB);
        recyclerViewB.setLayoutManager(new LinearLayoutManager(this));

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

        LoadUsers("");
        // Delay the first ad load to ensure some interaction time
        handler.postDelayed(this::loadAd, MIN_INTERACTION_TIME);
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
                        Toast.makeText(FindFriendActivity.this, "Ad failed to load", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(FindFriendActivity.this, "Reward earned: " + rewardAmount + " coins", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FindFriendActivity.this, "Failed to update coins", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    adShowing = false;
                    lastAdShownTime = System.currentTimeMillis();
                    adsShownThisSession++;
                    // Delay the next ad load to ensure some interaction time
                    handler.postDelayed(FindFriendActivity.this::loadAd, MIN_INTERACTION_TIME);
                }
            });
        } else if (mRewardedInterstitialAd == null) {
            Toast.makeText(this, "The rewarded interstitial ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
            adShowing = false;
            loadAd();
        }
    }

    private void LoadUsers(String s) {
        Query query = mUserRef.orderByChild("names").startAt(s).endAt(s + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query, Users.class).build();
        adapter = new FirebaseRecyclerAdapter<Users, FindFriendViewHold>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHold holder, int i, @NonNull Users model) {
                if (!user.getUid().equals(getRef(i).getKey())) {
                    Picasso.get().load(model.getProfileImage()).into(holder.profileImageff);
                    holder.usernameff.setText(model.getNames());
                    holder.professionff.setText(model.getProfession());

                } else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FindFriendActivity.this, ViewFriendActivity.class);
                        intent.putExtra("usersKey", getRef(i).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FindFriendViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_find_friend, parent, false);
                return new FindFriendViewHold(view);
            }
        };
        adapter.startListening();
        recyclerViewB.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LoadUsers(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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
        mRewardedInterstitialAd = null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cleanup ad references when the activity is destroyed
        mRewardedInterstitialAd = null;
    }

}


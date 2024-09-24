package com.lemon.check.checkesandchess.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.Utils.Users;
import com.lemon.check.checkesandchess.databinding.ActivityRewardBinding;

import org.jetbrains.annotations.NotNull;

public class



RewardActivity extends AppCompatActivity {

    ActivityRewardBinding  binding;
    private RewardedAd mRewardedAd;
    FirebaseDatabase database;
    ///
    FirebaseAuth mAuth;
    DatabaseReference mUserRef;
    FirebaseUser user;
    ///
    String currentUid;
    int coins= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRewardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        currentUid = FirebaseAuth.getInstance().getUid();
        ///
        FirebaseAuth.getInstance();
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        loadAd();

        if (currentUser!=null)
        {
            database.getReference().child("Users")
                    .child(currentUid)
                    .child("coins")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            coins = snapshot.getValue(Integer.class);
                            binding.coins.setText(String.valueOf(coins));

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

        }else {
            Toast.makeText(this, "Please Signup To Continue", Toast.LENGTH_SHORT).show();
            finish();

        }

        binding.video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedAd != null) {
                    Activity activityContext = RewardActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            loadAd();
                            coins = coins + 5;
                            database.getReference().child("Users")
                                    .child(currentUid)
                                    .child("coins")
                                    .setValue(coins);
                            binding.video1Icon.setImageResource(R.drawable.check);
                        }
                    });
                } else {
                    Toast.makeText(RewardActivity.this, "Please Try Again.........", Toast.LENGTH_SHORT).show();

                }
            }
        });


       // return false;
    }

    void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-1134102830016252/9256539612",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.

                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });
    }


}
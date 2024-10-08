package com.lemon.check.Evacheck.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
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
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.Evacheck.Accesstoken;
import com.lemon.check.Evacheck.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewFriendActivity extends AppCompatActivity {
    DatabaseReference mUserRef, requestRef, friendRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String profileImageUrI, names, gender, city, country;

    FirebaseUser user;
    long coins = 0;
    FirebaseDatabase database;
    String currentUid;


    CircleImageView profileImg;
    TextView userName, Gendre, address;
    Button btnPerform, btnDecline;
    ImageView btn_send_like;

    String CurrentState = "nothing_happen";
    String profession;
    String userId;
    String requestId;
    RequestQueue requestQueue;

    String otherUserId;

    String myprofileImageUrI, myusername, mygender, mycity, mycountry, myprofession;


    private RewardedInterstitialAd mRewardedInterstitialAd;
    private boolean adIsLoading;
    private boolean adShowing; // Flag to track if an ad is currently showing
    private long lastAdShownTime; // To track the last ad shown time
    private static final long AD_COOLDOWN_TIME = 300000; // 5 minutes cooldown
    private static final long MIN_INTERACTION_TIME = 60000; // 1 minute minimum interaction time
    private static final int MAX_ADS_PER_SESSION = 3; // Maximum ads per session
    private int adsShownThisSession = 0; // Counter for ads shown in the current session
    private Handler handler = new Handler(); // Handler for delaying ad loading
    private boolean isLiked = false; // Track like status


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        userId = getIntent().getStringExtra("usersKey");


        getSupportActionBar().setTitle("ViewFriend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        otherUserId = getIntent().getStringExtra("otherUserId");

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //.child(userID);
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btnPerform = findViewById(R.id.btnPerform);
        btnDecline = findViewById(R.id.btnDecline);
        btn_send_like = findViewById(R.id.btn_send_like);

        profileImg = findViewById(R.id.profileImg);
        userName = findViewById(R.id.userName);
        Gendre = findViewById(R.id.Gendre);
        address = findViewById(R.id.address);


        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        currentUid = FirebaseAuth.getInstance().getUid();

        btn_send_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    toggleLike(userId);
            }
        });

        // Initialize the currentState based on the current status in the database if needed
        checkLikeStatus(userId);

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


        LoadUser();

        // Delay the first ad load to ensure some interaction time
        handler.postDelayed(this::loadAd, MIN_INTERACTION_TIME);

        btnPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(userId);
            }
        });
        CheckUserExistance(userId);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unfriend(userId);
            }
        });


    }

    private void toggleLike(String userId) {
        if (CurrentState.equals("nothing_happen")) {
            sendLike(userId);
        } else if (CurrentState.equals("like")) {
            removeLike(userId);
        }
    }
    private void sendLike(String userId) {
        // Check if a friend request is already sent
        requestRef.child(userId).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("status")) {
                    String status = snapshot.child("status").getValue(String.class);
                    if ("friend_request_sent".equals(status)) {
                        Toast.makeText(ViewFriendActivity.this, "Cannot send like. Friend request already sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Proceed with checking coin balance and sending the like
                        checkCoinBalanceAndSendLike(userId);
                    }
                } else {
                    // Proceed with checking coin balance and sending the like
                    checkCoinBalanceAndSendLike(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, "Error checking friend request status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCoinBalanceAndSendLike(String userId) {
        // Check the user's coin balance
        mUserRef.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("coins")) {
                    int coins = snapshot.child("coins").getValue(Integer.class);
                    if (coins >= 10) {
                        coins = coins - 20;
                        // Proceed with sending the like
                        sendLikeAction(userId, coins);
                    } else {
                        Toast.makeText(ViewFriendActivity.this, "Not enough coins to send a like! 20 coins required!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewFriendActivity.this, "Error retrieving coin balance", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, "Error checking coin balance", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendLikeAction(String userId, int coins) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "like");
        requestRef.child(mUser.getUid()).child(userId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mUserRef.child(mUser.getUid()).child("coins").setValue(coins).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> innerTask) {
                            if (innerTask.isSuccessful()) {
                                CurrentState = "like";
                                btn_send_like.setColorFilter(Color.RED); // Change icon tint to red
                                SendNotification(userId);
                                Toast.makeText(ViewFriendActivity.this, "Like Sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ViewFriendActivity.this, "Error deducting coins", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ViewFriendActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeLike(String userId) {
        requestRef.child(mUser.getUid()).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CurrentState = "nothing_happen";
                    btn_send_like.setColorFilter(Color.BLUE); // Change icon tint to blue
                    Toast.makeText(ViewFriendActivity.this, "Like Removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewFriendActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkLikeStatus(String userId) {
        requestRef.child(mUser.getUid()).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("status")) {
                    String status = dataSnapshot.child("status").getValue(String.class);
                    if ("like".equals(status)) {
                        CurrentState = "like";
                        btn_send_like.setColorFilter(Color.RED); // Change icon tint to red
                        // Disable friend request button if needed
                    } else if ("friend_request_sent".equals(status)) {
                        CurrentState = "friend_request_sent";
                        // Disable like button if needed
                        btn_send_like.setEnabled(false);
                    } else {
                        CurrentState = "nothing_happen";
                        btn_send_like.setColorFilter(Color.BLUE); // Change icon tint to blue
                    }
                } else {
                    CurrentState = "nothing_happen";
                    btn_send_like.setColorFilter(Color.BLUE); // Change icon tint to blue
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Check the receiver's ability to send a friend request or like
        requestRef.child(userId).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("status")) {
                    String status = snapshot.child("status").getValue(String.class);
                    if ("like".equals(status) || "friend_request_sent".equals(status)) {
                        // Disable friend request and like buttons for receiver if needed
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
    private void SendNotification(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String otherUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("FCMTokens");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(otherUserId);

        tokensRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String token = snapshot.getValue(String.class);
                    if (token != null) {
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                String otherUserName = userSnapshot.child("names").getValue(String.class);
                                String profilePictureUrl = userSnapshot.child("profileImage").getValue(String.class);
                                if (otherUserName != null) {
                                    sendNotification(token, "Like", "You have a new like  from " + otherUserName, profilePictureUrl);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("fetchAndSendNotification", "Error fetching user data: " + error.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fetchAndSendNotification", "Error fetching FCM token: " + error.getMessage());
            }
        });
    }

    private void sendNotification1(String token, String title, String body, String profilePictureUrl) {
        try {
            JSONObject mainObject = new JSONObject();
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            JSONObject dataObject = new JSONObject();

            notificationObject.put("title", title);
            notificationObject.put("body", body);
            notificationObject.put("image", profilePictureUrl); // Add image URL to notification

            dataObject.put("type", "like");
            dataObject.put("userId", userId);
            dataObject.put("profilePictureUrl", profilePictureUrl); // Add profile picture URL to data

            messageObject.put("token", token);
            messageObject.put("notification", notificationObject);
            messageObject.put("data", dataObject);

            mainObject.put("message", messageObject);

            callApi(mainObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                        Toast.makeText(ViewFriendActivity.this, "Ad failed to load", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ViewFriendActivity.this, "Reward earned: " + rewardAmount + " coins", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ViewFriendActivity.this, "Failed to update coins", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    adShowing = false;
                    lastAdShownTime = System.currentTimeMillis();
                    adsShownThisSession++;
                    // Delay the next ad load to ensure some interaction time
                    handler.postDelayed(ViewFriendActivity.this::loadAd, MIN_INTERACTION_TIME);
                }
            });
        } else if (mRewardedInterstitialAd == null) {
            Toast.makeText(this, "The rewarded interstitial ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
            adShowing = false;
            loadAd();
        }
    }

    private void Unfriend(String userId) {
        if (CurrentState.equals("friend"))
        {
            friendRef.child(mUser.getUid()).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        friendRef.child(userId).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(ViewFriendActivity.this, "Your have Unfriended", Toast.LENGTH_SHORT).show();
                                    CurrentState="nothing_happen";
                                    btnPerform.setText("Send Friend Request");
                                    btnDecline.setVisibility(View.GONE);
                                }

                            }
                        });
                    }
                }
            });
        }
        if (CurrentState.equals("he_sent_pending"))
        {
            HashMap hashMap=new HashMap();
            hashMap.put("status","decline");
            requestRef.child(userId).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ViewFriendActivity.this, "You have Declined Friend Request", Toast.LENGTH_SHORT).show();
                        CurrentState="he_sent_decline";
                        btnPerform.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                    }

                }
            });
        }
    }
    ///
    private void CheckUserExistance(String userId) {
        friendRef.child(mUser.getUid()).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CurrentState="friend";
                    btnPerform.setText("Send Message");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        friendRef.child(userId).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    CurrentState="friend";
                    btnPerform.setText("Send Message");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(mUser.getUid()).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("status").getValue().toString().equals("pending"))
                    {
                        CurrentState="I_sent_pending";
                        btnPerform.setText("Cancel Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                    if (snapshot.child("status").getValue().toString().equals("decline"))
                    {
                        CurrentState="I_sent_decline";
                        btnPerform.setText("Cancel Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(userId).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("status").getValue().toString().equals("pending"))
                    {
                        CurrentState= "he_sent_pending";
                        btnPerform.setText("Accept Friend Request");
                        btnDecline.setText("Decline Friend Request");
                        btnDecline.setVisibility(View.VISIBLE);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (CurrentState.equals("nothing_happen"))
        {
            CurrentState="nothing_happen";
            btnPerform.setText("Send Friend Request");
            btnDecline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String userId) {
        if (CurrentState.equals("nothing_happen"))
        {
            HashMap hashMap =new HashMap();
            hashMap.put("status","pending");
            requestRef.child(mUser.getUid()).child(userId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ViewFriendActivity.this, "Friend Request Sent", Toast.LENGTH_SHORT).show();
                        // Fetch the other user's FCM token and send the notification
                        fetchAndSendNotification(userId);
                        btnDecline.setVisibility(View.GONE);
                        CurrentState="I_sent_pending";
                        btnPerform.setText("Cancel Friend Request");
                    }
                    else
                    {
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (CurrentState.equals("I_sent_pending") || CurrentState.equals("I_sent_decline"))
        {
            requestRef.child(mUser.getUid()).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ViewFriendActivity.this, "You have cancelled Friend Request", Toast.LENGTH_SHORT).show();
                        CurrentState="nothing_happen";
                        btnPerform.setText("Send Fried Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (CurrentState.equals("he_sent_pending"))
        {
            requestRef.child(userId).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        HashMap hashMap =new HashMap();
                        hashMap.put("status","friend");
                        hashMap.put("names",names);
                        hashMap.put("profileImageUrI",profileImageUrI);
                        hashMap.put("profession",profession);
                        hashMap.put("requestId",requestId);
                        friendRef.child(mUser.getUid()).child(userId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    friendRef.child(userId).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            NotifyFriend(userId);
                                            Toast.makeText(ViewFriendActivity.this, "You added friend", Toast.LENGTH_SHORT).show();
                                            CurrentState="friend";
                                            btnPerform.setText("Send Message");
                                            btnDecline.setText("Unfriend");
                                            btnDecline.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
        if (CurrentState.equals("friend"))
        {
            Intent intent=new Intent(ViewFriendActivity.this, ChatActivity.class);
            intent.putExtra("otherUserId", this.userId);
            startActivity(intent);
        }
    }
    private void NotifyFriend(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String otherUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("FCMTokens");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(otherUserId);

        tokensRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String friendToken = snapshot.getValue(String.class);
                if (friendToken != null) {
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            String otherUserName = userSnapshot.child("names").getValue(String.class);
                            String profilePictureUrl = userSnapshot.child("profileImage").getValue(String.class);
                            if (otherUserName != null) {
                                sendNotificationToToken(friendToken, "Friend Request Accepted", "You are now friends with " + otherUserName, profilePictureUrl);
                            } else {
                                Log.e("NotifyFriend", "User name is null");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("NotifyFriend", "Error fetching user data: " + error.getMessage());
                        }
                    });
                } else {
                    Log.e("NotifyFriend", "Friend token is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotifyFriend", "Error fetching FCM token: " + error.getMessage());
            }
        });
    }

    private void sendNotificationToToken(String token, String title, String message, String profilePictureUrl) {
        try {
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            JSONObject dataObject = new JSONObject();

            notificationObject.put("title", title);
            notificationObject.put("body", message);
            notificationObject.put("image", profilePictureUrl); // Add image URL to notification

            dataObject.put("type", "friend_request_accepted");
            dataObject.put("userId", userId);
            dataObject.put("profilePictureUrl", profilePictureUrl); // Add profile picture URL to data


            messageObject.put("token", token);
            messageObject.put("notification", notificationObject);
            messageObject.put("data", dataObject);

            JSONObject mainObject = new JSONObject();
            mainObject.put("message", messageObject);

            Log.d("sendNotificationToToken", "Payload: " + mainObject.toString());

            callApi(mainObject);
        } catch (JSONException e) {
            Log.e("sendNotificationToToken", "Error creating JSON payload: " + e.getMessage());
        }
    }

    private void fetchAndSendNotification(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String otherUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("FCMTokens");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(otherUserId);

        tokensRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String token = snapshot.getValue(String.class);
                    if (token != null) {
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                String otherUserName = userSnapshot.child("names").getValue(String.class);
                                String profilePictureUrl = userSnapshot.child("profileImage").getValue(String.class);
                                if (otherUserName != null) {
                                    sendNotification(token, "Friend Request", "You have a new friend request from " + otherUserName, profilePictureUrl);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("fetchAndSendNotification", "Error fetching user data: " + error.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fetchAndSendNotification", "Error fetching FCM token: " + error.getMessage());
            }
        });
    }

    private void sendNotification(String token, String title, String body, String profilePictureUrl) {
        try {
            JSONObject mainObject = new JSONObject();
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            JSONObject dataObject = new JSONObject();

            notificationObject.put("title", title);
            notificationObject.put("body", body);
            notificationObject.put("image", profilePictureUrl); // Add image URL to notification

            dataObject.put("type", "friend_request");
            dataObject.put("userId", userId);
            dataObject.put("profilePictureUrl", profilePictureUrl); // Add profile picture URL to data

            messageObject.put("token", token);
            messageObject.put("notification", notificationObject);
            messageObject.put("data", dataObject);

            mainObject.put("message", messageObject);

            callApi(mainObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callApi(JSONObject payload) {
        new Thread(() -> {
            try {
                // Get the access token
                String accessToken = Accesstoken.getAccessToken();
                if (accessToken == null) {
                    Log.e("callApi", "Access token is null");
                    return;
                }

                // Create the HTTP request
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(payload.toString(), MediaType.parse("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url("https://fcm.googleapis.com/v1/projects/checki-fee94/messages:send")
                        .post(body)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .addHeader("Content-Type", "application/json; UTF-8")
                        .build();

                // Execute the request
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.d("callApi", "Notification sent successfully");
                } else {
                    Log.e("callApi", "Failed to send notification: " + response.body().string());
                }
            } catch (Exception e) {
                Log.e("callApi", "Error sending notification: " + e.getMessage(), e);
            }
        }).start();
    }


    private void LoadUser() {
        mUserRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())

                {
                    profileImageUrI=snapshot.child("profileImage").getValue().toString();
                    names=snapshot.child("names").getValue().toString();
                    gender=snapshot.child("gender").getValue().toString();
                    city=snapshot.child("city").getValue().toString();
                    country=snapshot.child("country").getValue().toString();
                    profession=snapshot.child("profession").getValue().toString();

                    Picasso.get().load(profileImageUrI).into(profileImg);
                    userName.setText(names);
                    Gendre.setText(gender);
                    address.setText(city+","+country);


                    profileImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewFullImage(profileImageUrI);
                        }
                    });


                }
                else {
                    Toast.makeText(ViewFriendActivity.this,"Data not found",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this,""+error.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void viewFullImage(String imageUr) {
        Intent intent =new Intent(this, FullScreenImageActivity.class);
        intent.putExtra("imageUrl", imageUr);
        startActivity(intent);

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

}

package com.lemon.check.Evacheck.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.lemon.check.Evacheck.models.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFriendRequests;
    private FriendRequestAdapter adapter;
    private ArrayList<User> requestList;
    private DatabaseReference requestRef, userRef, friendRef;
    private FirebaseUser currentUser;
    String ProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        getSupportActionBar().setTitle("Pending Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
           // return false;
        }

        recyclerViewFriendRequests = findViewById(R.id.recyclerViewPendingRequests);
        recyclerViewFriendRequests.setLayoutManager(new LinearLayoutManager(this));
        requestList = new ArrayList<>();
        adapter = new FriendRequestAdapter(requestList);
        recyclerViewFriendRequests.setAdapter(adapter);

        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends");

        loadFriendRequests();
       // return false;
    }

    private void loadFriendRequests() {
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            requestRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestList.clear();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if (userSnapshot.hasChild(currentUserId)) {
                            DataSnapshot currentUserRequest = userSnapshot.child(currentUserId);
                            if (currentUserRequest.hasChild("status") && "pending".equals(currentUserRequest.child("status").getValue(String.class))) {
                                String otherUserId = userSnapshot.getKey();
                                if (otherUserId != null) {
                                    userRef.child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.getValue(User.class);
                                            if (user != null) {
                                                user.setUserId(otherUserId); // Set userId in the user object
                                                requestList.add(user);
                                                adapter.notifyDataSetChanged();
                                            }
                                           // return null;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(FriendRequestActivity.this, "Failed to load user details.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                   // return null;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(FriendRequestActivity.this, "Failed to load friend requests.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void acceptFriendRequest(String otherUserId, int position) {
        if (currentUser == null || otherUserId == null || otherUserId.isEmpty()) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = requestList.get(position);
        if (user == null) {
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (position >= requestList.size() || position < 0) {
            Toast.makeText(this, "Invalid position", Toast.LENGTH_SHORT).show();
            return;
        }

        requestRef.child(otherUserId).child(currentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("status", "friend");
                    hashMap.put("names",user.getNames());
                    hashMap.put("profileImageUrI",user.getProfileImage());
                    hashMap.put("profession",user.getProfession());

                    friendRef.child(currentUser.getUid()).child(otherUserId).updateChildren(hashMap).addOnCompleteListener(innerTask -> {
                        if (innerTask.isSuccessful()) {
                            friendRef.child(otherUserId).child(currentUser.getUid()).updateChildren(hashMap).addOnCompleteListener(innermostTask -> {
                                if (innermostTask.isSuccessful()) {
                                    adapter.notifyItemRemoved(position);
                                    NotifyFriend(otherUserId);
                                    Toast.makeText(FriendRequestActivity.this, "Friend request accepted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FriendRequestActivity.this, "Failed to update friend list", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(FriendRequestActivity.this, "Failed to update friend list", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(FriendRequestActivity.this, "Failed to remove request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void NotifyFriend(String otherUserId) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("FCMTokens");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        tokensRef.child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String friendToken = snapshot.getValue(String.class);
                if (friendToken != null) {
                    Log.d("NotifyFriend", "Friend Token: " + friendToken);
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            String otherUserName = userSnapshot.child("names").getValue(String.class);
                            String profilePictureUrl = userSnapshot.child("profileImage").getValue(String.class);
                            if (otherUserName != null) {
                                sendNotificationToToken(friendToken, "Friend Request Accepted", "You are now friends with " + otherUserName, profilePictureUrl, otherUserId);
                            } else {
                                Log.e("NotifyFriend", "Other user's name is null");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("NotifyFriend", "Error fetching user data: " + error.getMessage());
                        }
                    });
                } else {
                    Log.e("NotifyFriend", "Friend token is null for user ID: " + otherUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotifyFriend", "Error fetching FCM token: " + error.getMessage());
            }
        });
    }
    private void sendNotificationToToken(String token, String title, String message, String profilePictureUrl, String otherUserId) {
        try {
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            JSONObject dataObject = new JSONObject();

            notificationObject.put("title", title);
            notificationObject.put("body", message);
            notificationObject.put("image", profilePictureUrl); // Add image URL to notification

            dataObject.put("type", "friend_request_accepted");
            dataObject.put("otherUserId", otherUserId); // Add otherUserId to data payload
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

    private void callApi(JSONObject payload) {
        new Thread(() -> {
            try {
                // Get the access token
                String accessToken = Accesstoken.getAccessToken();
                if (accessToken == null) {
                    Log.e("callApi", "Access token is null");
                    return;
                }

                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, payload.toString());
                Request request = new Request.Builder()
                        .url("https://fcm.googleapis.com/v1/projects/checki-fee94/messages:send")
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.d("callApi", "Notification sent successfully: " + response.body().string());
                } else {
                    Log.e("callApi", "Error sending notification: " + response.code() + " " + response.body().string());
                }
            } catch (Exception e) {
                Log.e("callApi", "Exception sending notification: " + e.getMessage());
            }
        }).start();

    }

    private void declineFriendRequest(String otherUserId, int position) {
        if (currentUser == null || otherUserId == null || otherUserId.isEmpty()) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            return;
        }

            if (position >= requestList.size() || position < 0) {
                Toast.makeText(this, "Invalid position", Toast.LENGTH_SHORT).show();
                return;
            }

        requestRef.child(otherUserId).child(currentUser.getUid()).removeValue().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Toast.makeText(FriendRequestActivity.this, "Friend request declined", Toast.LENGTH_SHORT).show();
                userRef.child(otherUserId).child("Requests").child(currentUser.getUid()).removeValue();
                adapter.notifyItemRemoved(position);
            } else {
                Toast.makeText(FriendRequestActivity.this, "Failed to decline request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

        private ArrayList<User> usersList;

        public FriendRequestAdapter(ArrayList<User> usersList) {
            this.usersList = usersList;
        }

        @NonNull
        @Override
        public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_friend_request, parent, false);
            return new FriendRequestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
            User user = usersList.get(position);
            holder.userName.setText(user.getNames());
            holder.userDetails.setText(String.format("%s, %s, %s",user.getGender(), user.getCity(), user.getCountry()));
            Picasso.get().load(user.getProfileImage()).into(holder.profileImage);

            holder.btnAccept.setOnClickListener(v -> acceptFriendRequest(user.getUserId(), position));
            holder.btnDecline.setOnClickListener(v -> declineFriendRequest(user.getUserId(), position));
        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }

        public class FriendRequestViewHolder extends RecyclerView.ViewHolder {

            CircleImageView profileImage;
            TextView userName, userDetails;
            Button btnAccept, btnDecline;

            public FriendRequestViewHolder(@NonNull View itemView) {
                super(itemView);
                profileImage = itemView.findViewById(R.id.profileImage);
                userName = itemView.findViewById(R.id.userName);
                userDetails = itemView.findViewById(R.id.userDetails);
                btnAccept = itemView.findViewById(R.id.btnAccept);
                btnDecline = itemView.findViewById(R.id.btnDecline);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

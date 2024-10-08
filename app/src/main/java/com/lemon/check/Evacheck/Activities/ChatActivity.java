package com.lemon.check.Evacheck.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.Evacheck.Accesstoken;
import com.lemon.check.Evacheck.Checkers.BlackCheckerActivity;
import com.lemon.check.Evacheck.Checkers.Room;
import com.lemon.check.Evacheck.Checkers.RoomManager;
import com.lemon.check.Evacheck.Player;
import com.lemon.check.Evacheck.R;
import com.lemon.check.Evacheck.Utils.Chat;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText inputMessage;
    CircleImageView profileCircleImageView;

    ImageView send_image, sendMessageImageView;

    TextView usernameOnAppbarTv, statusOnAppbarTv;


    String otherUserId, otherUsername, otherUserProfileImageLink, otherUserStatus;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference userReference, messageReference;
    private DatabaseReference refRoom;
    private DatabaseReference messageCountReference;

    private DatabaseReference typingStatusRef;

    FirebaseRecyclerOptions<Chat> options;
    FirebaseRecyclerAdapter<Chat, ChatMyViewHolder> adapter;
    // private FirebaseRecyclerAdapter<Chat, RecyclerView.ViewHolder> adapter;

    String myProfileImageLink;

    TextView typingIndicator;
    ImageView onlineIndicator;
    TextView send_challenge, statusTextView;



    // Declare a boolean flag to indicate if the user is typing
    private boolean isTyping = false;
    // Timer to reset the typing status
    private Timer typingTimer = new Timer();
    private final long TYPING_DELAY = 2000; // 2 seconds delay after user stops typing

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PERMISSION = 3;
    private static final int PICK_VIDEO = 4;

    String Player;
    String status;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    // Uri imageUri;


    Dialog replyDialog;
    private static final String TAG = "ChatActivity"; // Define TAG as a class-level constant
    private long messageCount;

    String names;


    private Player currentUser;
    private Player otherUser;
    String username;
    private SharedPreferences prefs;
    private boolean shouldClose = false;

    long coins = 0;
    String Users;
    DatabaseReference mUserRef;
    private FirebaseDatabase database;

    private DatabaseReference blockListRef;
    private ValueEventListener blockListListener; // Declare blockListListener here

    String message;

    private Uri imageUri; // Class-level variable for imageUri
    ImageView imageViewPreview;
    String otherUserToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = findViewById(R.id.Appa_bar);


        otherUserId = getIntent().getStringExtra("otherUserId");

        typingStatusRef = FirebaseDatabase.getInstance().getReference().child("TypingStatus");


        profileCircleImageView = findViewById(R.id.userProfileImageAppbar);
        inputMessage = findViewById(R.id.inputSms);
        send_image = findViewById(R.id.IImageview);
        sendMessageImageView = findViewById(R.id.BTNsend);
        onlineIndicator = findViewById(R.id.onlineIndicator);
        typingIndicator = findViewById(R.id.typingIndicator);
        send_challenge = findViewById(R.id.send_challenge);
        statusTextView = findViewById(R.id.status);
        imageViewPreview = findViewById(R.id.ImageViewPrevieww);

        usernameOnAppbarTv = findViewById(R.id.usernameAppbar);
        //statusOnAppbarTv = findViewById(R.id.statusTvChatActivityAppBar);


        recyclerView = findViewById(R.id.recyclerViewchat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        messageReference = FirebaseDatabase.getInstance().getReference().child("Message");
        messageCountReference = FirebaseDatabase.getInstance().getReference().child("MessageCount");

        // Initialize Firebase reference
        refRoom = FirebaseDatabase.getInstance().getReference("Room/available");

        // Assume current user and other user IDs are passed from previous activity
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String otherUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String currentUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String otherUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get current user's display name
        String currentUserDisplayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        // Create Player objects using the user IDs
        // Retrieve currentUser and otherUser from the intent
        currentUser = (Player) getIntent().getSerializableExtra("currentUser");
        otherUser = (Player) getIntent().getSerializableExtra("otherUser");

      // create Player object using the display name
        currentUser = new Player(currentUserDisplayName, currentUserDisplayName);





        // Check if user IDs are properly passed
        if (currentUserId == null || otherUserId == null) {
            // Set click listener to the profile image and username

            Toast.makeText(this, "User information is missing. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            // return false;
        }



        // Call this method when the user starts typing
        startTyping(currentUserId);

        // Call this method when the user stops typing
        stopTyping(currentUserId);

        // Listen for other user's typing status
        listenForTypingStatus(currentUserId);

        displayUserStatus(otherUserId);
        loadOtherUser();
        loadMyProfile();
        markMessagesAsSeenRealTime();

        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUse = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ////
        mUserRef.getDatabase().getReference().child("Users")
                .child(currentUse.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Long coinsValue = snapshot.child("coins").getValue(Long.class);
                            if (coinsValue != null) {
                                coins = coinsValue;
                                //Cooins.setText("You have: " + coins);
                            } else {
                                Log.e("ChatActivity", "Coins value is null");
                            }
                        } else {
                            Log.e("ChatActivity", "User snapshot does not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ChatActivity", "Database error: " + error.getMessage());
                    }
                });

        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    startTyping(currentUserId);
                } else {
                    stopTyping(currentUserId);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed
            }
        });

        send_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coins > 9) {
                    coins = coins - 10;
                    mUserRef.getDatabase().getReference().child("Users")
                            .child(currentUse.getUid())
                            .child("coins")
                            .setValue(coins);

                    createNewRoomAndStartGame(currentUser, otherUser);

                } else {
                    Toast.makeText(ChatActivity.this, "Insufficient Coins Watch Reward Ads To Earn Coins!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        send_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });




        sendMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageUri != null) {
                    // Send image message
                    sendImageMessage(imageUri);
                } else {
                    sendMessage();
                }

            }
        });

        loadMessage();


    }

    private void listenForTypingStatus(String currentUserId) {
        typingStatusRef.child(otherUserId).child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean isTyping = snapshot.getValue(Boolean.class);
                    if (isTyping != null && isTyping) {
                        // Show typing indicator
                        typingIndicator.setVisibility(View.VISIBLE);
                    } else {
                        // Hide typing indicator
                        typingIndicator.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatActivity", "Failed to listen for typing status: " + error.getMessage());
            }
        });
    }

    private void stopTyping(String currentUserId) {
        typingStatusRef.child(currentUserId).child(otherUserId).setValue(false);
    }

    private void startTyping(String currentUserId) {
        typingStatusRef.child(currentUserId).child(otherUserId).setValue(true);
    }


    private void viewUserDetails(String otherUserId) {
        Intent intent = new Intent(this, ViewFriendActivity.class);
        intent.putExtra("usersKey", otherUserId);
        startActivity(intent);
    }


    private void createNewRoomAndStartGame(Player currentUser, Player otherUser) {
        RoomManager roomManager = new RoomManager();
        Room room = roomManager.createRoom(currentUser); // Create room with currentUser as player1 (black checkers)
        room.setAvailability(true);
        // Pass both players' details


        // Save the room to the database
        refRoom.child(String.valueOf(room.getId())).setValue(room).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (otherUserId != null) {
                    // Room created successfully
                    // Notify the other user to close ChatActivity and start the game
                    sendGameStartNotification(otherUserId, room);
                    // Start game for currentUser (BlackCheckerActivity)
                    Intent intent = new Intent(ChatActivity.this, BlackCheckerActivity.class);
                    intent.putExtra("Room ID", room.getId()); // Pass the room ID to the activity
                    intent.putExtra("room", room);
                    startActivity(intent);
                    finish(); // Close ChatActivity for the current user
                } else {
                    Toast.makeText(ChatActivity.this, "Other user ID is missing.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChatActivity.this, "Unable to create room. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendGameStartNotification(String otherUserId, Room room) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tokensRef = database.getReference("FCMTokens").child(otherUserId);

        tokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String otherUserToken = snapshot.getValue(String.class);
                    if (otherUserToken != null) {
                        // Prepare the notification payload
                        JSONObject payload = new JSONObject();
                        JSONObject messageObject = new JSONObject();
                        JSONObject notificationBody = new JSONObject();
                        JSONObject data = new JSONObject();

                        try {
                            notificationBody.put("title", "Game Start");
                            notificationBody.put("body", "You have been invited to a game. Room ID: " + room.getId());

                            data.put("type", "gameStart");
                            data.put("roomId", String.valueOf(room.getId())); // Ensure room ID is a string

                            messageObject.put("token", otherUserToken);
                            messageObject.put("notification", notificationBody);
                            messageObject.put("data", data);

                            payload.put("message", messageObject);
                        } catch (JSONException e) {
                            Log.e("sendGameStartNotification", "JSON Exception: " + e.getMessage());
                        }

                        // Send the notification
                        callApi(payload);
                    } else {
                        Log.e("sendGameStartNotification", "FCM token for user " + otherUserId + " is null");
                    }
                } else {
                    Log.e("sendGameStartNotification", "Other user token not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("sendGameStartNotification", "Failed to fetch other user's token: " + error.getMessage());
            }
        });
    }

    private void displayUserStatus(String otherUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(otherUserId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String status = snapshot.child("status").getValue(String.class);
                    if (status != null && status.equals("online")) {
                        statusTextView.setText(status);
                        onlineIndicator.setVisibility(View.VISIBLE); // Show online indicator
                    } else if (status != null) {
                        // User is offline, calculate last online time
                        long lastOnline = Long.parseLong(status);
                        String lastOnlineTime = calculateLastOnline(lastOnline);
                        statusTextView.setText("Last online: " + lastOnlineTime);
                        onlineIndicator.setVisibility(View.GONE); // Hide online indicator
                    }
                }
                // return null;
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

   }
    private String calculateLastOnline(long lastOnline) {
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifferenceMillis = currentTimeMillis - lastOnline;
        // Convert milliseconds to minutes, hours, or days
        // Here's just a simple example, you can customize it based on your needs
        if (timeDifferenceMillis < 60000) {
            return "Less than a minute ago";
        } else if (timeDifferenceMillis < 3600000) {
            long minutes = timeDifferenceMillis / 60000;
            return minutes + " minutes ago";
        } else if (timeDifferenceMillis < 86400000) {
            long hours = timeDifferenceMillis / 3600000;
            return hours + " hours ago";
        } else {
            long days = timeDifferenceMillis / 86400000;
            return days + " days ago";
        }

    }

    protected void onStart() {
        super.onStart();
        // App comes to foreground, user is considered online
        if (otherUserId != null) {
            setUserOnlineStatus(otherUserId);
        }
        resetMessageCountsAndMarkSeen();

    }


    private void resetMessageCountsAndMarkSeen() {
        DatabaseReference messageRef = messageReference.child(otherUserId).child(firebaseUser.getUid());

        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null && !chat.isSeen()) {
                        snapshot.getRef().child("seen").setValue(true);
                    }
                }

                // Reset the message count for the current user
                DatabaseReference messageCountRef = FirebaseDatabase.getInstance().getReference().child("MessageCount")
                        .child(firebaseUser.getUid()).child(otherUserId);
                messageCountRef.setValue(0);
                //return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatActivity", "Failed to reset message counts and mark seen: " + databaseError.getMessage());
            }
        });
    }

    private void markMessagesAsSeenRealTime() {
        DatabaseReference messageRef = messageReference.child(otherUserId).child(firebaseUser.getUid());

        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateSeenStatus(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateSeenStatus(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // No action needed for this case
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // No action needed for this case
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatActivity", "Failed to listen for message changes: " + databaseError.getMessage());
            }
        });
    }

    private void updateSeenStatus(DataSnapshot dataSnapshot) {
        Chat chat = dataSnapshot.getValue(Chat.class);
        if (chat != null && !chat.isSeen()) {
            dataSnapshot.getRef().child("seen").setValue(true);
        }

    }

    private void setUserOnlineStatus(String otherUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(otherUserId).child("status");
        userRef.setValue("online");
    }


    @Override
    protected void onStop() {
        super.onStop();

        // App goes to background, user is considered offline
        if (otherUserId != null) {
            setUserOfflineStatus(otherUserId);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (otherUserId != null) {
            setUserOfflineStatus(otherUserId);
        }
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        stopTyping(currentUserId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (otherUserId != null) {
            setUserOfflineStatus(otherUserId);
        }
    }

    private void setUserOfflineStatus(String otherUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(otherUserId);
        userRef.child("status").setValue(String.valueOf(System.currentTimeMillis()));

    }


    private void sendImageMessage(Uri imageUri) { // Remove Uri parameter, use class-level variable instead
        if (this.imageUri == null) {
            Toast.makeText(ChatActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setMessage("Sending image...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        // Get the current timestamp
        long currentTime = System.currentTimeMillis();

        // Generate a unique key for the message
        String messageId = messageReference.child(firebaseUser.getUid()).child(otherUserId).push().getKey();

        // Create a HashMap to store message details
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", this.imageUri.toString());
        messageMap.put("userId", firebaseUser.getUid());
        messageMap.put("messageId", messageId);
        messageMap.put("otherUserId", firebaseUser.getDisplayName());
        messageMap.put("messageType", "image"); // Add messageType field
        messageMap.put("timestamp", currentTime); // Add timestamp field
        messageMap.put("seen", false); // Set seen status to false initially

        // Update the Firebase database with the message details
        messageReference.child(otherUserId).child(firebaseUser.getUid()).child(messageId).updateChildren(messageMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Update the sender's node
                            messageReference.child(firebaseUser.getUid()).child(otherUserId).child(messageId).updateChildren(messageMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                // Clear the ImageView
                                                ImageView imageViewPreview = findViewById(R.id.ImageViewPrevieww);
                                                imageViewPreview.setImageDrawable(null); // Clear the image
                                                ChatActivity.this.imageUri = null; // Reset imageUri
                                                // Display a success message
                                                incrementMessageCount(otherUserId);
                                                sendNotification(message,otherUserId,userId);
                                                Toast.makeText(ChatActivity.this, "Image message sent", Toast.LENGTH_SHORT).show();
                                                // Handle the success case
                                            } else {
                                                progressDialog.dismiss();
                                                ChatActivity.this.imageUri = null; // Reset imageUri
                                                // Handle the error
                                                Toast.makeText(ChatActivity.this, "Failed to send image message", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            ChatActivity.this.imageUri = null; // Reset imageUri
                            // Handle the error
                            Toast.makeText(ChatActivity.this, "Failed to send image message", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
             }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_PICK:
                if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    // Get the selected image URI
                    imageUri = data.getData();
                    Glide.with(this).load(imageUri).into(imageViewPreview);
                    // Show or hide the ImageView as needed
                    imageViewPreview.setVisibility(View.VISIBLE);
                }
                break;
            case PICK_VIDEO:
                if (resultCode == RESULT_OK && data != null) {
                    /**  // Get the selected VIDEO URI
                     videoUri=data.getData();
                     videoView.setVisibility(View.VISIBLE);
                     videoView.setVideoURI(videoUri);
                     videoView.setMediaController(mediaController);
                     videoView.start();**/

                }
                break;
        }

    }

    private void loadMyProfile() {
        userReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myProfileImageLink = snapshot.child("profileImage").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessage() {

        options = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(messageReference.
                child(firebaseUser.getUid()).child(otherUserId), Chat.class).build();
        adapter = new FirebaseRecyclerAdapter<Chat, ChatMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatMyViewHolder holder, int position, @NonNull Chat model) {

                    profileCircleImageView.setOnClickListener(view -> viewUserDetails(otherUserId));
                    usernameOnAppbarTv.setOnClickListener(view -> viewUserDetails(otherUserId));


                    holder.firstUserProfile.setOnClickListener(view -> viewUserDetails(otherUserId));
                    holder.secondUserProfile.setOnClickListener(v -> myprofiledetails(userId));


                    // Set long press listener for each message item
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // Show dialog to provide delete option
                            showDeleteDialog(position);
                            return true; // Consume the long click event

                        }
                    });

                    // Set image click listener
                    if (model.getMessageType().equals("image")) {
                        holder.firstUserImage.setOnClickListener(view -> viewFullImage(model.getMessage()));
                        holder.secondUserImage.setOnClickListener(view -> viewFullImage(model.getMessage()));
                    }

                    // Determine if the message is from the current user or the other user
                    boolean isCurrentUser = model.getUserId().equals(firebaseUser.getUid());

                    if (isCurrentUser) {
                        // Handle the current user's message
                        holder.firstUserText.setVisibility(View.GONE);
                        holder.firstUserTime.setVisibility(View.GONE);
                        holder.firstUserProfile.setVisibility(View.GONE);
                        holder.firstUserImage.setVisibility(View.GONE);

                        holder.secondUserText.setVisibility(View.VISIBLE);
                        holder.secondUserTime.setVisibility(View.VISIBLE);
                        holder.secondUserProfile.setVisibility(View.VISIBLE);
                        holder.secondUserImage.setVisibility(View.VISIBLE);

                        holder.secondUserText.setText(model.getMessage());
                        holder.secondUserTime.setText(getFormattedTime(model.getTimestamp()));

                        // Handle image message visibility
                        if (model.getMessageType().equals("image")) {
                            Picasso.get().load(model.getMessage()).into(holder.secondUserImage);
                            holder.secondUserText.setVisibility(View.GONE);
                        } else {
                            holder.secondUserImage.setVisibility(View.GONE);
                        }

                        // Load sender's profile image
                        Picasso.get().load(myProfileImageLink).into(holder.secondUserProfile);

                        // Update the visibility of the "seen" indicator based on the "seen" status of the message
                        if (model.isSeen()) {
                            holder.secondUserSeenStatus.setVisibility(View.VISIBLE); // Show for current user's messages if seen by the other user
                        } else {
                            holder.secondUserSeenStatus.setVisibility(View.GONE);
                        }

                        // Hide the seen indicator for the first user (as this is the current user's message)
                        holder.firstUserSeenStatus.setVisibility(View.GONE);

                    } else {
                        // Handle the other user's message
                        holder.firstUserText.setVisibility(View.VISIBLE);
                        holder.firstUserTime.setVisibility(View.VISIBLE);
                        holder.firstUserProfile.setVisibility(View.VISIBLE);
                        holder.firstUserImage.setVisibility(View.VISIBLE);

                        holder.secondUserText.setVisibility(View.GONE);
                        holder.secondUserTime.setVisibility(View.GONE);
                        holder.secondUserProfile.setVisibility(View.GONE);
                        holder.secondUserImage.setVisibility(View.GONE);

                        holder.firstUserText.setText(model.getMessage());
                        holder.firstUserTime.setText(getFormattedTime(model.getTimestamp()));

                        // Handle image message visibility
                        if (model.getMessageType().equals("image")) {
                            Picasso.get().load(model.getMessage()).into(holder.firstUserImage);
                            holder.firstUserText.setVisibility(View.GONE);
                        } else {
                            holder.firstUserImage.setVisibility(View.GONE);
                        }

                        // Load other user's profile image
                        Picasso.get().load(otherUserProfileImageLink).into(holder.firstUserProfile);

                        // Hide the seen indicator for the other user's messages
                        holder.firstUserSeenStatus.setVisibility(View.GONE);

                        // Also hide the seen indicator for the second user (as this is not the current user's message)
                        holder.secondUserSeenStatus.setVisibility(View.GONE);
                    }
                }

                @NonNull
                @Override
                public ChatMyViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview_sms, parent, false);
                    return new ChatMyViewHolder(view);

                }
            }

            ;

        adapter.startListening();
        recyclerView.setAdapter(adapter);

        }



    private void myprofiledetails(String userId) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("otherUserId", userId);
        startActivity(intent);

    }

    private void viewFullImage(String message) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra("imageUrl", message);
        startActivity(intent);
    }


    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle("Delete Message");
        builder.setMessage("Are you sure you want to delete this message?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the message from the RecyclerView and associated data source
                deleteMessage(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    // Method to delete message from RecyclerView and associated data source
    private void deleteMessage(int position) {
        // Remove the item from the data list
        adapter.getSnapshots().getSnapshot(position).getRef().removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Message deleted successfully
                        Toast.makeText(ChatActivity.this, "Message deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to delete message
                        Toast.makeText(ChatActivity.this, "Failed to delete message", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void sendMessage() {
        String message = inputMessage.getText().toString();
        if (message.isEmpty()) {
            inputMessage.setError("Please write something");
            inputMessage.setFocusable(true);
        } else {

            // Get the current timestamp
            long currentTime = System.currentTimeMillis();
            HashMap hashMap = new HashMap();
            hashMap.put("message", message);
            hashMap.put("userId", firebaseUser.getUid());
            hashMap.put("String otherUserId", firebaseUser.getDisplayName());
            hashMap.put("messageId", messageReference.child(firebaseUser.getUid()).child(otherUserId).push().getKey());
            hashMap.put("messageType", "text"); // Add messageType field
            hashMap.put("timestamp", currentTime); // Add timestamp field
            hashMap.put("seen", false); // Set seen status to false initially


            messageReference.child(otherUserId).child(firebaseUser.getUid()).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        messageReference.child(firebaseUser.getUid()).child(otherUserId).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    inputMessage.setText(null);
                                    sendNotification(message,otherUserId,userId);
                                    incrementMessageCount(otherUserId);
                                    Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void incrementMessageCount(String otherUserId) {
        DatabaseReference messageCountRef = FirebaseDatabase.getInstance().getReference().child("MessageCount")
                .child(otherUserId).child(firebaseUser.getUid());

        messageCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long messageCount = 0;
                if (dataSnapshot.exists()) {
                    messageCount = dataSnapshot.getValue(Long.class);
                }
                messageCount++;
                messageCountRef.setValue(messageCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatActivity", "Failed to increment message count: " + databaseError.getMessage());
            }
        });

    }


    private void sendNotification(String message, String otherUserId,String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tokensRef = database.getReference("FCMTokens").child(otherUserId);
        DatabaseReference usersRef = database.getReference("Users").child(userId);

        tokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherUserToken = snapshot.getValue(String.class);
                if (otherUserToken != null) {
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String otherUserName = snapshot.child("names").getValue(String.class);
                            String profilePictureUrl = snapshot.child("profileImage").getValue(String.class);
                            if (otherUserName != null) {
                                try {
                                    JSONObject messageObject = new JSONObject();
                                    JSONObject notificationObject = new JSONObject();
                                    JSONObject dataObject = new JSONObject();

                                    // Construct the notification payload
                                    notificationObject.put("title", "Message from " + otherUserName);
                                    notificationObject.put("body", message);
                                    notificationObject.put("image", profilePictureUrl); // Add image URL to notification


                                    // Construct the data payload
                                    dataObject.put("type", "message");
                                    dataObject.put("userId", userId);
                                    dataObject.put("profilePictureUrl", profilePictureUrl); // Add profile picture URL to data

                                    // Construct the message object
                                    messageObject.put("token", otherUserToken);
                                    messageObject.put("notification", notificationObject);
                                    messageObject.put("data", dataObject);

                                    // Construct the full payload
                                    JSONObject payload = new JSONObject();
                                    payload.put("message", messageObject);

                                    callApi(payload);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void callApi(JSONObject payload) {
        new Thread(() -> {
            try {
                // Get the access token
                String accessToken = Accesstoken.getAccessToken();
                if (accessToken == null) {
                    Log.e("sendNotification", "Access token is null");
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
                    Log.d("sendNotification", "Notification sent successfully");
                } else {
                    Log.e("sendNotification", "Failed to send notification: " + response.body().string());
                }
            } catch (Exception e) {
                Log.e("sendNotification", "Error sending notification: " + e.getMessage(), e);
            }
        }).start();
    }

    private void loadOtherUser() {
        userReference.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    otherUsername = snapshot.child("names").getValue().toString();
                    otherUserProfileImageLink = snapshot.child("profileImage").getValue().toString();
                    Picasso.get().load(otherUserProfileImageLink).into(profileCircleImageView);
                    usernameOnAppbarTv.setText(otherUsername);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_clear_messages) {
            clearAllMessages();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearAllMessages() {
        // Implement logic to delete all messages from the chat
        // Get a reference to the message node in the database
        DatabaseReference messageNodeRef = FirebaseDatabase.getInstance().getReference().child("Message")
                .child(firebaseUser.getUid())  // Current user's messages
                .child(otherUserId);           // Messages with the other user

        // Remove all messages under this node
        messageNodeRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Messages deleted successfully
                        Toast.makeText(ChatActivity.this, "All messages deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to delete messages
                        Toast.makeText(ChatActivity.this, "Failed to delete messages", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String getFormattedTime(long currentTime) {
        // Convert timestamp to a readable time format
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        // Create a Date object from the timestamp
        Date date = new Date(currentTime);
        // Format the date object to display only the time
        return sdf.format(date);
    }

}





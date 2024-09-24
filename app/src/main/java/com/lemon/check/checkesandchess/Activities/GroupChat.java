package com.lemon.check.checkesandchess.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.adapters.GroupChatAdapter;
import com.lemon.check.checkesandchess.models.GroupChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChat extends AppCompatActivity implements GroupChatAdapter.OnMessageLongClickListener {

    private RecyclerView recyclerView;
    private GroupChatAdapter adapter;
    private List<GroupChatMessage> messagesList;
    private DatabaseReference databaseReference, creatorReference;
    private ImageButton btnSend, btnPickImage;
    String groupId;
    String uid;
    private String createdByUserId;

    private ImageView publishdBtn;
    private EditText editTextMessage;


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private static final int PERMISSION=3;
    private static final int PICK_VIDEO=4;



    // Add member variables to store the counts
    private int newMessageCount = 0;
    private int unreadMessageCount = 0;



    // Add a member variable to store the URI of the selected image
    Uri imageUri = null; // Declare imageUri as a class member variable
    Uri videoUri;
    private VideoView videoView;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);



        recyclerView = findViewById(R.id.recycler_view_group_chat);
        btnSend = findViewById(R.id.btn_send);
        btnPickImage = findViewById(R.id.btn_pick_image);
        editTextMessage = findViewById(R.id.edit_text_message);
        publishdBtn = findViewById(R.id.admobdBtn);
        videoView=findViewById(R.id.videoView0);

        messagesList = new ArrayList<>();
        adapter = new GroupChatAdapter(this, messagesList);
        adapter.setOnMessageLongClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mediaController = new MediaController(GroupChat.this);


        getSupportActionBar().setTitle("Group Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        if (groupId == null || groupId.isEmpty()) {
            Toast.makeText(this, "Invalid Group ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("messages");
        retrieveMessages();

        checkPermission();
        // Fetch and set group information in the toolbar
        fetchGroupInfo(groupId);
        publishdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupChat.this, "On the upcoming update", Toast.LENGTH_SHORT).show();
                //showPublishDialog();
            }
        });

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    // Send image message
                    sendImageMessage(imageUri);
                }else if (videoUri != null){
                    // Send video message
                    sendVideoMessage(videoUri);
                }

                else {
                    // Send text message
                    sendMessage();
                }
            }
        });
        // return false;
    }

    private void sendVideoMessage(Uri videoUri) {
        if (videoUri != null) {
            // Show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Video...");
            progressDialog.setMessage("Please wait while we upload and send the video.");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();

            // Upload the video to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("videos");
            StorageReference videoRef = storageRef.child("group_videos/" + UUID.randomUUID().toString());

            videoRef.putFile(videoUri)
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setProgress((int) progress);
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded video
                        videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Construct the video message
                            String messageId = databaseReference.push().getKey();
                            String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            long timestamp = System.currentTimeMillis();
                            String messageType = "video";
                            String imageUrl = ""; // No image URL for video messages
                            String videoUrl = uri.toString();
                            String message = ""; // No message content for video messages
                            String groupId = getIntent().getStringExtra("groupId");
                            String replyMessageId = ""; // No reply message for video messages
                            int viewCount = 0;

                            GroupChatMessage chatMessage = new GroupChatMessage(messageId,
                                    senderId, imageUrl,videoUrl, timestamp, message, messageType, groupId, replyMessageId,viewCount);

                            // Save the video message to the Firebase Realtime Database
                            databaseReference.child(messageId).setValue(chatMessage)
                                    .addOnSuccessListener(aVoid -> {
                                        // Close progress dialog
                                        progressDialog.dismiss();
                                        // Show success message

                                        // Clear the VideoView
                                        videoView.setVisibility(View.GONE);
                                        videoView.setVideoURI(null);
                                        videoView.setMediaController(null);
                                        incrementMessageCount();
                                        Toast.makeText(GroupChat.this, "Video sent successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Close progress dialog
                                        progressDialog.dismiss();
                                        // Show failure message
                                        Toast.makeText(GroupChat.this, "Failed to send video", Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Close progress dialog
                        progressDialog.dismiss();
                        // Show failure message
                        Toast.makeText(GroupChat.this, "Failed to upload video", Toast.LENGTH_SHORT).show();
                        Log.e("Upload Error", "Failed to upload video", e);
                    });
        } else {
            // Video URI is null, handle accordingly
            Toast.makeText(GroupChat.this, "No video selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPublishDialog() {
        Dialog dialog =new Dialog(GroupChat.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.upload_dialog);
        dialog.setCanceledOnTouchOutside(true);

        TextView txt_upload_video=dialog.findViewById(R.id.txt_upload_video);
        txt_upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent,"select video"),PICK_VIDEO);


            }
        });


        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (currentUserId != null) {
            resetMessageCounts(currentUserId);
        }

    }

    private void resetMessageCounts(String currentUserId) {
        DatabaseReference messageCountRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupMessageCount")
                .child(groupId);
        messageCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    messageCountRef.setValue(0);
                } else {
                    Log.e("GroupChat", "Message count does not exist for user: " + currentUserId);
                }
                // return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("GroupChat", "Failed to reset message count: " + databaseError.getMessage());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_groupinfo) {
            // Navigate to the GroupInfoActivity
            Intent intent = new Intent(GroupChat.this, GroupInfoActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
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
                    // Update the UI by loading the selected image into the ImageView
                    ImageView imageViewPreview = findViewById(R.id.imageViewPreview);
                    Glide.with(this).load(imageUri).into(imageViewPreview);
                    // Show or hide the ImageView as needed
                    imageViewPreview.setVisibility(View.VISIBLE);
                }
                break;
            case PICK_VIDEO:
                if (resultCode== RESULT_OK && data !=null){
                    // Get the selected VIDEO URI
                    videoUri=data.getData();
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(videoUri);
                    videoView.setMediaController(mediaController);
                    videoView.start();

                }
                break;
        }

    }
    private <imageUri> void sendImageMessage(Uri imageUri) {
        // Check if the URI is valid
        if (this.imageUri != null) {
            // Show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image...");
            progressDialog.setMessage("Please wait while we upload and send the image.");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();

            // Upload the image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");
            StorageReference imageRef = storageRef.child("group_images/" + UUID.randomUUID().toString());

            imageRef.putFile(this.imageUri)
                    .addOnProgressListener(taskSnapshot -> {
                        // Update progress dialog with the current upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setProgress((int) progress);
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Send the image message with the download URL
                            String messageId = databaseReference.push().getKey();
                            String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            long timestamp = System.currentTimeMillis();
                            String messageType = "image";
                            String videoUrl="";
                            String imageUrl = uri.toString();
                            String message = "";
                            String groupId = getIntent().getStringExtra("groupId");
                            String replyMessageId = "";
                            int viewCount = 0;

                            GroupChatMessage chatMessage = new GroupChatMessage(messageId,
                                    senderId, imageUrl,videoUrl, timestamp, message, messageType,groupId,replyMessageId,viewCount);

                            databaseReference.child(messageId).setValue(chatMessage)
                                    .addOnSuccessListener(aVoid -> {
                                        editTextMessage.setText("");
                                        // Clear the ImageView
                                        // Clear the ImageView
                                        ImageView imageViewPreview = findViewById(R.id.imageViewPreview);
                                        imageViewPreview.setImageDrawable(null); // Clear the image
                                        this.imageUri = null; // Reset the imageUri

                                        // Close progress dialog
                                        progressDialog.dismiss();
                                        // Show success message
                                        incrementMessageCount();
                                        Toast.makeText(GroupChat.this, "Image sent successfully", Toast.LENGTH_SHORT).show();
                                        // Reset imageUri after sending the image message


                                    })
                                    .addOnFailureListener(e -> {
                                        // Close progress dialog
                                        ImageView imageViewPreview = findViewById(R.id.imageViewPreview);
                                        imageViewPreview.setImageDrawable(null); // Clear the image
                                         this.imageUri = null; // Reset the imageUri
                                        progressDialog.dismiss();
                                        // Handle failure
                                        Toast.makeText(GroupChat.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Close progress dialog
                        progressDialog.dismiss();
                        // Handle failure
                        Toast.makeText(GroupChat.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Image URI is null, handle accordingly
            Toast.makeText(GroupChat.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchGroupInfo(String groupId) {
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String groupName = dataSnapshot.child("groupTitle").getValue(String.class);
                    String groupIconUrl = dataSnapshot.child("groupIcon").getValue(String.class);
                    createdByUserId = dataSnapshot.child("createdBy").getValue(String.class);
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    // Check if the current user is the creator
                    if (currentUserId.equals(createdByUserId)) {
                        // User is the creator, allow access to the AdMob button
                        publishdBtn.setVisibility(View.VISIBLE);

                    } else {
                        // User is not the creator, hide the AdMob button
                        publishdBtn.setVisibility(View.GONE);
                    }

                    // Set group name
                    TextView groupNameTextView = findViewById(R.id.group_name_text_view);
                    groupNameTextView.setText(groupName);
                    // Load group icon using Glide
                    CircleImageView groupIconImageView = findViewById(R.id.group_icon_image_view);
                    Glide.with(GroupChat.this)
                            .load(groupIconUrl)
                            .placeholder(R.drawable.ic_person1) // Placeholder image while loading
                            .into(groupIconImageView);
                }
                //return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        String messageId = databaseReference.push().getKey();
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();
        String messageType = "text";
        String imageUrl = "";
        String videoUrl="";
        String groupId = getIntent().getStringExtra("groupId");
        String replyMessageId = "";
        int viewCount=0;

        GroupChatMessage chatMessage = new GroupChatMessage(messageId, senderId,
                imageUrl,videoUrl, timestamp, message, messageType,groupId,replyMessageId,viewCount);

        databaseReference.child(messageId).setValue(chatMessage)

                .addOnSuccessListener(aVoid -> {
                    editTextMessage.setText("");
                    Toast.makeText(GroupChat.this, "Message sent", Toast.LENGTH_SHORT).show();
                    incrementMessageCount(); })
                .addOnFailureListener(e -> Toast.makeText(GroupChat.this,
                        "Failed to send message", Toast.LENGTH_SHORT).show());
    }

    private void incrementMessageCount() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference messageCountRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupMessageCount").child(groupId);

        messageCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long messageCount = 0;
                if (dataSnapshot.exists()) {
                    messageCount = dataSnapshot.getValue(Long.class);
                }
                messageCount++;
                messageCountRef.setValue(messageCount);
                //return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("GroupChat", "Failed to increment message count: " + databaseError.getMessage());
            }
        });

    }

    private void retrieveMessages() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                GroupChatMessage message = snapshot.getValue(GroupChatMessage.class);

                // Fetch view count along with other message details
                DatabaseReference viewCountRef = FirebaseDatabase.getInstance()
                        .getReference("Groups")
                        .child(groupId)
                        .child("messages")
                        .child(message.getMessageId())
                        .child("viewCount");

                // Fetch view count
                viewCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            long viewCount = dataSnapshot.getValue(Long.class);
                            message.setViewCount((int) viewCount);
                        } else {
                            // View count not found, set it to 0
                            message.setViewCount(0);
                        }

                        // Add the message to the list
                        messagesList.add(message);
                        adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        //return null;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }

            // Other methods of ChildEventListener are not needed for this implementation
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onMessageLongClicked(GroupChatMessage message) {
        showLongPressDialog(message);
    }

    private void showLongPressDialog(GroupChatMessage message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message Options");
        builder.setItems(new CharSequence[]{"Delete", "Reply"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    deleteMessage(message);
                    break;
                case 1:
                    showReplyDialog(message);
                    break;
            }
        });
        builder.create().show();
    }

    private void showReplyDialog(GroupChatMessage message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reply, null);
        builder.setView(dialogView);

        EditText editTextReply = dialogView.findViewById(R.id.edit_text_reply);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSendReply = dialogView.findViewById(R.id.btn_send_reply);

        AlertDialog replyDialog = builder.create();

        btnCancel.setOnClickListener(view -> replyDialog.dismiss());

        btnSendReply.setOnClickListener(view -> {
            String replyMessage = editTextReply.getText().toString().trim();
            if (!replyMessage.isEmpty()) {
                sendMessageReply(message, replyMessage);
                replyDialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a reply message", Toast.LENGTH_SHORT).show();
            }
        });

        replyDialog.show();
    }

    private void sendMessageReply(GroupChatMessage originalMessage, String replyMessage) {
        // Create a new GroupChatMessage for the reply
        String messageId = databaseReference.push().getKey();
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();
        String messageType = "reply";
        String imageUrl = "";
        String videoUrl="";
        String groupId = getIntent().getStringExtra("groupId");
        String replyMessageId = originalMessage.getMessageId();
        int viewCount=0;

        GroupChatMessage replyChatMessage = new GroupChatMessage(messageId,
                senderId, imageUrl,videoUrl, timestamp, replyMessage, messageType, groupId, replyMessageId,viewCount);
        replyChatMessage.setReplyMessageId(originalMessage.getMessageId()); // Attach the reply message to the original message

        // Update the original message in the database with the reply message ID
        originalMessage.setReplyMessageId(messageId);
        DatabaseReference originalMessageRef = databaseReference.child(originalMessage.getMessageId());
        originalMessageRef.setValue(originalMessage)
                .addOnSuccessListener(aVoid -> {
                    // Send the reply message
                    databaseReference.child(messageId).setValue(replyChatMessage)
                            .addOnSuccessListener(aVoid1 -> {
                                editTextMessage.setText("");
                                incrementMessageCount();
                                Toast.makeText(GroupChat.this, "Reply sent successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(GroupChat.this, "Failed to send reply", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(GroupChat.this, "Failed to update original message", Toast.LENGTH_SHORT).show();
                });
    }


    private void deleteMessage(GroupChatMessage message) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Check if the current user is the creator or the message sender
        if (currentUserId.equals(createdByUserId) || currentUserId.equals(message.getSenderId())) {
            DatabaseReference messageRef = FirebaseDatabase.getInstance()
                    .getReference("Groups")
                    .child(groupId)
                    .child("messages")
                    .child(message.getMessageId());

            // Delete the message itself
            messageRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(GroupChat.this, "Message deleted", Toast.LENGTH_SHORT).show();

                        // If it's a video message, also delete its views count
                        if ("video".equals(message.getMessageType())) {
                            DatabaseReference viewsRef = FirebaseDatabase.getInstance()
                                    .getReference("Groups")
                                    .child(groupId)
                                    .child("messages")
                                    .child(message.getMessageId())
                                    .child("viewCount");
                            viewsRef.removeValue()
                                    .addOnSuccessListener(aVoid1 -> {
                                        Toast.makeText(GroupChat.this, "Views count deleted", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(GroupChat.this, "Failed to delete views count", Toast.LENGTH_SHORT).show();
                                    });
                        }

                        // Remove the message from the list and notify the adapter
                        messagesList.remove(message);
                        adapter.notifyDataSetChanged(); // Notify the adapter after deleting the message
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(GroupChat.this, "Failed to delete message", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "You do not have permission to delete this message", Toast.LENGTH_SHORT).show();
        }
    }
    private void  checkPermission(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GroupChat.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION);
        }else {
            Log.d("tag", "checkPermission: Permission granted");
        }
    }

}


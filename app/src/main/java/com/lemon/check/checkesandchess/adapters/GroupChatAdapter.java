package com.lemon.check.checkesandchess.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.Activities.FullScreenImageActivity;
import com.lemon.check.checkesandchess.Activities.ViewFriendActivity;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.models.GroupChatMessage;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    private Context context;

    private static List<GroupChatMessage> messagesList;

    public GroupChatAdapter(Context context, List<GroupChatMessage> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    public interface OnMessageLongClickListener {
        void onMessageLongClicked(GroupChatMessage message);
    }

    private OnMessageLongClickListener longClickListener;

    public void setOnMessageLongClickListener(OnMessageLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_chat_message, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupChatMessage message = messagesList.get(position);
        holder.textMessage.setText(message.getMessage());
        holder.textTimestamp.setText(formatTimestamp(message.getTimestamp()));


        holder.imageMessage.setOnClickListener(v -> {
            // Handle image click event
            String imageUrl = messagesList.get(position).getImageUrl();
            Intent intent = new Intent(context, FullScreenImageActivity.class);
            intent.putExtra("imageUrl", imageUrl);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            // Notify the listener that a message has been long-clicked
            if (longClickListener != null) {
                longClickListener.onMessageLongClicked(messagesList.get(position));
            }
            return true;
        });

        // Check message type
        if (message.getMessageType() != null && message.getMessageType().equals("image")) {
            holder.textMessage.setVisibility(View.GONE);
            holder.imageMessage.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
            holder.playButton.setVisibility(View.GONE);
            holder.text_view_count.setVisibility(View.GONE);
            Glide.with(context).load(message.getImageUrl()).placeholder(R.drawable.gradient_connect).into(holder.imageMessage);
        } else if (message.getMessageType() != null && message.getMessageType().equals("video")) {
            holder.textMessage.setVisibility(View.GONE);
            holder.imageMessage.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.playButton.setVisibility(View.VISIBLE);
            holder.text_view_count.setVisibility(View.VISIBLE);
            holder.text_view_count.setText("Views: " + message.getViewCount());
            setupVideoPlayer(holder, message);
        } else if (message.getMessageType() != null && message.getMessageType().equals("reply")) {
            holder.textMessage.setVisibility(View.VISIBLE);
            holder.imageMessage.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.playButton.setVisibility(View.GONE);
            holder.text_view_count.setVisibility(View.GONE);
            holder.textMessage.setText("Reply: " + message.getMessage());
        } else {
            holder.textMessage.setVisibility(View.VISIBLE);
            holder.imageMessage.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.playButton.setVisibility(View.GONE);
            holder.text_view_count.setVisibility(View.GONE);
        }

        // Set sender name and profile picture
        String senderId = message.getSenderId();
        if (senderId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(message.getSenderId());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String senderName = dataSnapshot.child("names").getValue(String.class);
                        String profileImageUrl = dataSnapshot.child("profileImage").getValue(String.class);
                        holder.textSenderName.setText(senderName);
                        Glide.with(context).load(profileImageUrl).placeholder(R.drawable.ic_person1).into(holder.imageSenderProfile);
                    }
                    //return null;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    private void setupVideoPlayer(ViewHolder holder, GroupChatMessage message) {
        ExoPlayer player = new ExoPlayer.Builder(context).build();
        holder.videoView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(message.getVideoUrl()));
        player.setMediaItem(mediaItem);
        player.prepare();

        // Set play button click listener
        holder.playButton.setOnClickListener(v -> {
            player.play();
            holder.playButton.setVisibility(View.GONE);
        });

        // Track video view
        player.addListener(new Player.Listener() {
            private boolean isPlaying = false;

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY && !isPlaying) {
                    isPlaying = true;
                    incrementViewCount(message);
                }
                if (playbackState == Player.STATE_ENDED) {
                    isPlaying = false;
                }
            }
        });
    }

    private void incrementViewCount(GroupChatMessage message) {
        String groupId = message.getGroupId();
        String messageId = message.getMessageId();



        // Log the increment action
        Log.d("GroupChatAdapter", "Incrementing view count for messageId: " + messageId);

        // Increment view count for the message
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("GroupChatMessages").child(messageId);
        messageRef.child("viewCount").setValue(ServerValue.increment(1))
                .addOnSuccessListener(aVoid -> Log.d("GroupChatAdapter", "Message view count incremented successfully"))
                .addOnFailureListener(e -> Log.e("GroupChatAdapter", "Failed to increment message view count", e));

        // Increment total views for the group
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.child("totalViews").setValue(ServerValue.increment(1));
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageSenderProfile;
        TextView textSenderName;
        TextView textMessage;
        TextView textTimestamp;
        ImageView imageMessage;
        PlayerView videoView;
        ImageView playButton;
        TextView text_view_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSenderProfile = itemView.findViewById(R.id.image_sender_profile);
            textSenderName = itemView.findViewById(R.id.text_sender_name);
            textMessage = itemView.findViewById(R.id.text_message);
            textTimestamp = itemView.findViewById(R.id.text_timestamp);
            imageMessage = itemView.findViewById(R.id.imageMessage);
            videoView = itemView.findViewById(R.id.videoView);
            playButton = itemView.findViewById(R.id.play_button);
            text_view_count = itemView.findViewById(R.id.text_view_count);
        }
    }

    // Utility method to format timestamp
    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}

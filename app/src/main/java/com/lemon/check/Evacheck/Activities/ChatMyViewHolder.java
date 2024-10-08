package com.lemon.check.Evacheck.Activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemon.check.Evacheck.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMyViewHolder extends RecyclerView.ViewHolder {
    boolean isChat;
    CircleImageView firstUserProfile,secondUserProfile;
    TextView firstUserText,secondUserText,firstUserTime,firstUserSeenStatus,
            secondUserTime,secondUserSeenStatus,textViewCount_messages;
    ImageView secondUserImage,firstUserImage,firstUserReplyIndicator,secondUserReplyIndicator;

    // New views for reply preview
    View replyPreviewLayout;
    TextView repliedMessage;
    ImageView closeReplyPreview;

    public ChatMyViewHolder(@NonNull View itemView) {
        super(itemView);
        firstUserProfile=itemView.findViewById(R.id.firstUserProfile);
        secondUserProfile=itemView.findViewById(R.id.secondUserProfile);

        firstUserText=itemView.findViewById(R.id.firstUserText);
        secondUserText=itemView.findViewById(R.id.secondUserText);
        firstUserImage=itemView.findViewById(R.id.firstUserImage);
        secondUserImage=itemView.findViewById(R.id.secondUserImage);
        firstUserTime=itemView.findViewById(R.id.firstUserTime);
        firstUserSeenStatus=itemView.findViewById(R.id.firstUserSeenStatus);
        secondUserTime=itemView.findViewById(R.id.secondUserTime);
        secondUserSeenStatus=itemView.findViewById(R.id.secondUserSeenStatus);



        // Initialize the new views for the reply preview
        replyPreviewLayout = itemView.findViewById(R.id.replyPreviewLayout);
        repliedMessage = itemView.findViewById(R.id.repliedMessage);
        closeReplyPreview = itemView.findViewById(R.id.closeReplyPreview);


    }

}
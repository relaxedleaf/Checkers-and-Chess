package com.lemon.check.checkesandchess.Activities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemon.check.checkesandchess.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendMyViewHolder extends RecyclerView.ViewHolder{
    CircleImageView profileImageUrI;
    TextView username, profession,textView_message_count;
    public FriendMyViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImageUrI=itemView.findViewById(R.id.GroupProfileImg);
        username=itemView.findViewById(R.id.GroupTitle);
        profession=itemView.findViewById(R.id.GroupDescription);
        textView_message_count=itemView.findViewById(R.id.textView_message_count);

    }
}


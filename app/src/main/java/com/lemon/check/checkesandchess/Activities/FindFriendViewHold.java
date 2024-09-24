package com.lemon.check.checkesandchess.Activities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemon.check.checkesandchess.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendViewHold  extends RecyclerView.ViewHolder  {
    CircleImageView profileImageff;
    TextView usernameff,professionff;

    public FindFriendViewHold(@NonNull View itemView) {
        super(itemView);
        profileImageff=itemView.findViewById(R.id.Profile_find_friend);
        usernameff=itemView.findViewById(R.id.username_find_friend);
        professionff=itemView.findViewById(R.id.profession_find_friend);

    }
}

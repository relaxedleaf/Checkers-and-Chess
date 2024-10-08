package com.lemon.check.Evacheck.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.Evacheck.R;
import com.lemon.check.Evacheck.models.GroupModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserGroupsAdapter extends RecyclerView.Adapter<UserGroupsAdapter.ViewHolder> {

    private List<GroupModel> userGroupsList;

    String senderId;

    public UserGroupsAdapter(List<GroupModel> userGroupsList) {
        this.userGroupsList = userGroupsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupModel group = userGroupsList.get(position);

        holder.groupTitle.setText(group.getGroupTitle());
        holder.groupDescription.setText(group.getGroupDescription());
        Picasso.get().load(group.getGroupIcon()).into(holder.GroupProfL);

        // Fetch and display the message count
        fetchMessageCount(group.getGroupId(),  holder.textView_Group_message_count);

        // Add more binding if needed
    }

    private void fetchMessageCount(String groupId, TextView textViewGroupMessageCount) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference messageCountRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupMessageCount")
                .child(groupId);

        messageCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long messageCount = dataSnapshot.getValue(Long.class);
                    textViewGroupMessageCount.setText(String.valueOf(messageCount));
                } else {
                    textViewGroupMessageCount.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textViewGroupMessageCount.setText("Error");
            }
        });
    }

    @Override
    public int getItemCount() {
        return userGroupsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupTitle;
        TextView groupDescription;
        TextView textView_Group_message_count;
        CircleImageView GroupProfL;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.textViewGroupTitle);
            groupDescription = itemView.findViewById(R.id.textViewGroupDescription);
            GroupProfL=itemView.findViewById(R.id.GroupProfL);
            textView_Group_message_count=itemView.findViewById(R.id.textView_Group_message_count);

            GroupProfL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
    }
}


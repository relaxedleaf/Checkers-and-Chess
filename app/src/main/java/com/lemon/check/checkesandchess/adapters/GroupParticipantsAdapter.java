package com.lemon.check.checkesandchess.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lemon.check.checkesandchess.R;
import com.lemon.check.checkesandchess.Utils.Participant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupParticipantsAdapter extends RecyclerView.Adapter<GroupParticipantsAdapter.ParticipantViewHolder> {

    private List<Participant> participantList;
    private OnRemoveParticipantListener listener;
    String groupCreatorId;


    public GroupParticipantsAdapter(List<Participant> participantList, OnRemoveParticipantListener listener) {
        this.participantList = participantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_participants, parent, false);
        return new ParticipantViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        Participant participant = participantList.get(position);
        holder.textViewParticipantName.setText(participant.getNames());
        Glide.with(holder.itemView.getContext()).load(participant.getProfileImage()).into(holder.imageViewParticipant);

        // Set onClickListener for remove button
        holder.buttonRemoveParticipant.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveParticipant(participant);

            }
        });
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewParticipant;
        TextView textViewParticipantName;
        Button buttonRemoveParticipant;

        ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewParticipant = itemView.findViewById(R.id.image_participant);
            textViewParticipantName = itemView.findViewById(R.id.text_participant_name);
            buttonRemoveParticipant = itemView.findViewById(R.id.removeButton);
        }
    }

    public interface OnRemoveParticipantListener {
        void onRemoveParticipant(Participant participant);
    }
}

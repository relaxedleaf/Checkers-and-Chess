package com.lemon.check.checkesandchess.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.Utils.Users;
import com.lemon.check.checkesandchess.databinding.ActivityConnectingBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ConnectingActivity extends AppCompatActivity {
    ActivityConnectingBinding binding;
    boolean isOkay = false;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            // Handle case when user is not authenticated
            return;
        }
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Random").child(mUser.getUid());

        String profile = getIntent().getStringExtra("ProfileImage");
        Glide.with(this)
                .load(profile)
                .into(binding.profile);
        String username = mUser.getUid();

        mUserRef.getDatabase().getReference().child("Random")
                .orderByChild("status")
                .equalTo(0).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        if (snapshot.getChildrenCount() > 0) {
                            isOkay = true;
                            // Room Available
                            for (DataSnapshot childSnap : snapshot.getChildren()) {
                                if (!childSnap.child("createdBy").getValue(String.class).equals(username)) {
                                    mUserRef.getDatabase().getReference().child("Random")
                                            .child(childSnap.getKey())
                                            .child("incoming")
                                            .setValue(username);
                                    mUserRef.getDatabase().getReference().child("Random")
                                            .child(childSnap.getKey())
                                            .child("status")
                                            .setValue(1);
                                    Intent intent = new Intent(ConnectingActivity.this, CallActivity.class);
                                    String incoming = childSnap.child("incoming").getValue(String.class);
                                    String createdBy = childSnap.child("createdBy").getValue(String.class);
                                    boolean isAvailable = childSnap.child("isAvailable").getValue(Boolean.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("incoming", incoming);
                                    intent.putExtra("createdBy", createdBy);
                                    intent.putExtra("isAvailable", isAvailable);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // The found room is created by the current user, skip this room
                                    isOkay = false;
                                }
                            }
                        }

                        if (!isOkay) {
                            // Not Available or user is creator of found room
                            HashMap<String, Object> room = new HashMap<>();
                            room.put("incoming", username);
                            room.put("createdBy", username);
                            room.put("isAvailable", true);
                            room.put("status", 0);

                            mUserRef.getDatabase().getReference().child("Random")
                                    .child(username)
                                    .setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            mUserRef.getDatabase().getReference().child("Random")
                                                    .child(username).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                            if (snapshot.child("status").exists()) {
                                                                if (snapshot.child("status").getValue(Integer.class) == 1) {
                                                                    isOkay = true;
                                                                    Intent intent = new Intent(ConnectingActivity.this, CallActivity.class);
                                                                    String incoming = snapshot.child("incoming").getValue(String.class);
                                                                    String createdBy = snapshot.child("createdBy").getValue(String.class);
                                                                    boolean isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                                                                    intent.putExtra("username", username);
                                                                    intent.putExtra("incoming", incoming);
                                                                    intent.putExtra("createdBy", createdBy);
                                                                    intent.putExtra("isAvailable", isAvailable);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }
}

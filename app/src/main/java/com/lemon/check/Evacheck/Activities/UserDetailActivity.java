package com.lemon.check.Evacheck.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.Evacheck.R;
import com.squareup.picasso.Picasso;

public class UserDetailActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private  TextView usernameTextView, statusTextView ,countryTextView;
    Button btnUptcoins;


    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference userReference;
    String  otherUserId;
    long coins=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        profileImageView = findViewById(R.id.profileImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        statusTextView = findViewById(R.id.statusTextView);
        btnUptcoins=findViewById(R.id.btnUptcoins);
        countryTextView=findViewById(R.id.country);


        otherUserId = getIntent().getStringExtra("otherUserId");

        String otherUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        getSupportActionBar().setTitle("My Profile");



        loadUserDetails(otherUserId);
       // return false;
    }
    private void loadUserDetails(String otherUserId) {
        userReference.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("names").getValue(String.class);
                    String profileImage = snapshot.child("profileImage").getValue(String.class);
                    String status = snapshot.child("profession").getValue(String.class);
                    String country = snapshot.child("country").getValue(String.class);
                    Long coinsValue = snapshot.child("coins").getValue(Long.class);
                    if (coinsValue != null) {
                        coins = coinsValue;
                        btnUptcoins.setText("You have " + coins + " Coins");
                    }

                    usernameTextView.setText(username);
                    statusTextView.setText(status);
                    countryTextView.setText(country);




                    if (profileImage != null && !profileImage.isEmpty()) {
                        Picasso.get().load(profileImage).into(profileImageView);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDetailActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

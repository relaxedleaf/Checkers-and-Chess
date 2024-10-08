package com.lemon.check.Evacheck.Chess;

import android.content.Intent;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lemon.check.Evacheck.Activities.RewardActivity;
import com.lemon.check.Evacheck.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.Evacheck.R;

import java.util.ArrayList;
import java.util.List;

public class ChessRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refRoom;
    private DatabaseReference refUnavailableRoom;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    MediaPlayer clickSound;

    private Button btnCreate;
    private EditText etRoom;
    private Button btnJoin;
    private Player player;
    private Room room;
    private FirebaseUser user;
    DatabaseReference mUserRef;
    private RoomManager roomManager = new RoomManager();
    private List<Integer> unavailableRoomIdList = new ArrayList<>();
    ImageView BtnReward;
    TextView Coins;
    String Users;
    long coins=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_room);

        clickSound = MediaPlayer.create(ChessRoomActivity.this, R.raw.click);

//**********RecyclerView***********
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//**********************************

        btnCreate = findViewById(R.id.btnCreateRoomID);
        etRoom = findViewById(R.id.etRoomID);
        btnJoin = findViewById(R.id.btnjoinID);

        BtnReward=findViewById(R.id.BtnReward);
        Coins=findViewById(R.id.Coins);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        refRoom = database.getReference("ChessRoom").child("available");
        refUnavailableRoom = database.getReference("ChessRoom").child("unavailable");

        user = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        /////
        FirebaseUser currentUser=mAuth.getCurrentUser();


        if (currentUser!=null)
        {
            mUserRef.getDatabase().getReference().child("Users")
                    .child(currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Long coinsValue = snapshot.child("coins").getValue(Long.class);
                                if (coinsValue != null) {
                                    coins = coinsValue;
                                    Coins.setText("You have: " + coins);
                                } else {
                                    Log.e("ChessRoomActivity", "Coins value is null");
                                }
                            } else {
                                Log.e("ChessRoomActivity", "User snapshot does not exist");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ChessRoomActivity", "Database error: " + error.getMessage());
                        }
                    });
        }

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = mAuth.getCurrentUser();
            }
        });

        refSignUpPlayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = mAuth.getCurrentUser();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(user != null) {//(double check)when user sign up in the mainActivity, this listener will be called, but the activity has not been called, so there is no user
                        if (dataSnapshot1.getValue(Player.class).getUsername().equals(user.getDisplayName())) {
                            player = dataSnapshot1.getValue(Player.class);
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomManager.getRoomList().clear();
                roomManager.getIdList().clear();
                for(DataSnapshot roomSnapshot : dataSnapshot.getChildren()){
                    roomManager.getRoomList().add(roomSnapshot.getValue(Room.class));
                    roomManager.getIdList().add(roomSnapshot.getValue(Room.class).getId());

                }
                recyclerViewAdapter = new RecyclerViewAdapter(ChessRoomActivity.this, roomManager.getRoomList());
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refUnavailableRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                unavailableRoomIdList.clear();
                for(DataSnapshot roomSnapshot : dataSnapshot.getChildren()){
                    unavailableRoomIdList.add(roomSnapshot.getValue(Room.class).getId());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BtnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChessRoomActivity.this, RewardActivity.class);
                startActivity(intent);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coins > 4) {
                    coins = coins - 5;
                    mUserRef.getDatabase().getReference().child("Users")
                            .child(currentUser.getUid())
                            .child("coins")
                            .setValue(coins);
                    clickSound.start();
                    room = roomManager.createRoom(player);
                    refRoom.child(String.valueOf(room.getId())).setValue(room);
                    Intent intent = new Intent(ChessRoomActivity.this, RedChessActivity.class);
                    intent.putExtra("room", room);
                    startActivity(intent);
                }else {
                    Toast.makeText(ChessRoomActivity.this, "Insufficient Coins Watch Reward Ads To Earn Coins!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coins >4) {
                    coins = coins - 5;
                    mUserRef.getDatabase().getReference().child("Users")
                            .child(currentUser.getUid())
                            .child("coins")
                            .setValue(coins);
                    clickSound.start();
                    Boolean exist = false;
                    if (!etRoom.getText().toString().equals("")){
                        int roomNum = Integer.parseInt(etRoom.getText().toString());
                        if (roomNum != 0) {
                            for (int i = 0; i < roomManager.getRoomList().size(); i++) {
                                if (roomNum == roomManager.getRoomList().get(i).getId()) {
                                    exist = true;
                                    Room room = roomManager.getRoomList().get(i);
                                    room.setPlayer2(player);
                                    room.setAvailability(false);
                                    refRoom.child(String.valueOf(room.getId())).setValue(room);
                                    Intent intent = new Intent(ChessRoomActivity.this, BlackChessActivity.class);
                                    intent.putExtra("room", room);
                                    ChessRoomActivity.this.startActivity(intent);
                                    break;
                                }
                            }
                            if (exist == false) {
                                Toast.makeText(ChessRoomActivity.this, "Room Doesn't Exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else{//join random room
                        if(roomManager.getRoomList().size() == 0){
                            Toast.makeText(ChessRoomActivity.this, "No Available Rooms", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            int min = 0;
                            int max = roomManager.getRoomList().size();
                            int range = (roomManager.getRoomList().size() - 1) + 1;
                            int random = (int) (Math.random() * range) + min;
                            Room room = roomManager.getRoomList().get(random);
                            room.setPlayer2(player);
                            room.setAvailability(false);
                            refRoom.child(String.valueOf(room.getId())).setValue(room);
                            Intent intent = new Intent(ChessRoomActivity.this, BlackChessActivity.class);
                            intent.putExtra("room", room);
                            ChessRoomActivity.this.startActivity(intent);
                        }
                    }
                }else {
                    Toast.makeText(ChessRoomActivity.this, "Insufficient Coins Watch Reward Ads To Earn Coins!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}

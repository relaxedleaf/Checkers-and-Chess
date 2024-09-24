package com.lemon.check.checkesandchess.Chess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lemon.check.checkesandchess.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.R;


import java.util.List;


public class BlackChessActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton[][] imageButtonList;

    private Boolean firstClick = true;
    private List<List<Piece>> board;
    private List<Point> moves;
    private Point start;

    boolean isBlacksTurn;

    //*****
    private NullChess nullc = new NullChess();

    private Boolean backPressed = false;
    private Boolean paused = false;
    private Boolean waitingMessage = false;

    private int round = 0;//for tracking the number of turns, if round is even, show the toast saying "your turn"

    private AlertDialog alertDialog;
    private AlertDialog.Builder dialogBuilder;

    MediaPlayer clickSound;

    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refRoom;
    private DatabaseReference refThisRoom;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private RoomManager roomManager = new RoomManager();
    private Player player;//Red
    private Player player1;//Black
    private Room room;
    private boolean player1Exited = false;

    private TextView tvRoom;
    private TextView tvPlayer1Name;
    private TextView tvPlayer2Name;
    //******


    //row 1
    private ImageButton ibtn_0_0;
    private ImageButton ibtn_0_1;
    private ImageButton ibtn_0_2;
    private ImageButton ibtn_0_3;
    private ImageButton ibtn_0_4;
    private ImageButton ibtn_0_5;
    private ImageButton ibtn_0_6;
    private ImageButton ibtn_0_7;
    //row 2
    private ImageButton ibtn_1_0;
    private ImageButton ibtn_1_1;
    private ImageButton ibtn_1_2;
    private ImageButton ibtn_1_3;
    private ImageButton ibtn_1_4;
    private ImageButton ibtn_1_5;
    private ImageButton ibtn_1_6;
    private ImageButton ibtn_1_7;
    //row 3
    private ImageButton ibtn_2_0;
    private ImageButton ibtn_2_1;
    private ImageButton ibtn_2_2;
    private ImageButton ibtn_2_3;
    private ImageButton ibtn_2_4;
    private ImageButton ibtn_2_5;
    private ImageButton ibtn_2_6;
    private ImageButton ibtn_2_7;
    //row 4
    private ImageButton ibtn_3_0;
    private ImageButton ibtn_3_1;
    private ImageButton ibtn_3_2;
    private ImageButton ibtn_3_3;
    private ImageButton ibtn_3_4;
    private ImageButton ibtn_3_5;
    private ImageButton ibtn_3_6;
    private ImageButton ibtn_3_7;

    //row 5
    private ImageButton ibtn_4_0;
    private ImageButton ibtn_4_1;
    private ImageButton ibtn_4_2;
    private ImageButton ibtn_4_3;
    private ImageButton ibtn_4_4;
    private ImageButton ibtn_4_5;
    private ImageButton ibtn_4_6;
    private ImageButton ibtn_4_7;

    //row 6
    private ImageButton ibtn_5_0;
    private ImageButton ibtn_5_1;
    private ImageButton ibtn_5_2;
    private ImageButton ibtn_5_3;
    private ImageButton ibtn_5_4;
    private ImageButton ibtn_5_5;
    private ImageButton ibtn_5_6;
    private ImageButton ibtn_5_7;

    //row 7
    private ImageButton ibtn_6_0;
    private ImageButton ibtn_6_1;
    private ImageButton ibtn_6_2;
    private ImageButton ibtn_6_3;
    private ImageButton ibtn_6_4;
    private ImageButton ibtn_6_5;
    private ImageButton ibtn_6_6;
    private ImageButton ibtn_6_7;

    //row 8
    private ImageButton ibtn_7_0;
    private ImageButton ibtn_7_1;
    private ImageButton ibtn_7_2;
    private ImageButton ibtn_7_3;
    private ImageButton ibtn_7_4;
    private ImageButton ibtn_7_5;
    private ImageButton ibtn_7_6;
    private ImageButton ibtn_7_7;

    private Button btnSurrender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_chess);
        //*****
        clickSound = MediaPlayer.create(BlackChessActivity.this, R.raw.click);

        btnSurrender = findViewById(R.id.btnSurrenderID);
        tvRoom = findViewById(R.id.tvRoomIdID);
        tvPlayer1Name = findViewById(R.id.tvPlayer1ID);
        tvPlayer2Name = findViewById(R.id.tvPlayer2ID);

        tvPlayer2Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSound.start();
                if (player1Exited == false) {
                    dialogBuilder = new AlertDialog.Builder(BlackChessActivity.this);
                    View statusView = getLayoutInflater().inflate(R.layout.status, null);
                    TextView tvWin = statusView.findViewById(R.id.tvWinID);
                    TextView tvLoss = statusView.findViewById(R.id.tvLossID);
                    TextView tvWinningRate = statusView.findViewById(R.id.tvWinningRateID);

                    tvWin.setText("Win: " + player1.getWin());
                    tvLoss.setText("Loss: " + player1.getLoss());
                    tvWinningRate.setText("Winning Rate: " + player1.getWinningRate());

                    dialogBuilder.setView(statusView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        //*****


        //row 1
        ibtn_0_0 = findViewById(R.id.ibtn_0_0);
        ibtn_0_1 = findViewById(R.id.ibtn_0_1);
        ibtn_0_2 = findViewById(R.id.ibtn_0_2);
        ibtn_0_3 = findViewById(R.id.ibtn_0_3);
        ibtn_0_4 = findViewById(R.id.ibtn_0_4);
        ibtn_0_5 = findViewById(R.id.ibtn_0_5);
        ibtn_0_6 = findViewById(R.id.ibtn_0_6);
        ibtn_0_7 = findViewById(R.id.ibtn_0_7);


//row 2
        ibtn_1_0 = findViewById(R.id.ibtn_1_0);
        ibtn_1_1 = findViewById(R.id.ibtn_1_1);
        ibtn_1_2 = findViewById(R.id.ibtn_1_2);
        ibtn_1_3 = findViewById(R.id.ibtn_1_3);
        ibtn_1_4 = findViewById(R.id.ibtn_1_4);
        ibtn_1_5 = findViewById(R.id.ibtn_1_5);
        ibtn_1_6 = findViewById(R.id.ibtn_1_6);
        ibtn_1_7 = findViewById(R.id.ibtn_1_7);
//row 3
        ibtn_2_0 = findViewById(R.id.ibtn_2_0);
        ibtn_2_1 = findViewById(R.id.ibtn_2_1);
        ibtn_2_2 = findViewById(R.id.ibtn_2_2);
        ibtn_2_3 = findViewById(R.id.ibtn_2_3);
        ibtn_2_4 = findViewById(R.id.ibtn_2_4);
        ibtn_2_5 = findViewById(R.id.ibtn_2_5);
        ibtn_2_6 = findViewById(R.id.ibtn_2_6);
        ibtn_2_7 = findViewById(R.id.ibtn_2_7);
//row 4
        ibtn_3_0 = findViewById(R.id.ibtn_3_0);
        ibtn_3_1 = findViewById(R.id.ibtn_3_1);
        ibtn_3_2 = findViewById(R.id.ibtn_3_2);
        ibtn_3_3 = findViewById(R.id.ibtn_3_3);
        ibtn_3_4 = findViewById(R.id.ibtn_3_4);
        ibtn_3_5 = findViewById(R.id.ibtn_3_5);
        ibtn_3_6 = findViewById(R.id.ibtn_3_6);
        ibtn_3_7 = findViewById(R.id.ibtn_3_7);

//row 5
        ibtn_4_0 = findViewById(R.id.ibtn_4_0);
        ibtn_4_1 = findViewById(R.id.ibtn_4_1);
        ibtn_4_2 = findViewById(R.id.ibtn_4_2);
        ibtn_4_3 = findViewById(R.id.ibtn_4_3);
        ibtn_4_4 = findViewById(R.id.ibtn_4_4);
        ibtn_4_5 = findViewById(R.id.ibtn_4_5);
        ibtn_4_6 = findViewById(R.id.ibtn_4_6);
        ibtn_4_7 = findViewById(R.id.ibtn_4_7);

//row 6
        ibtn_5_0 = findViewById(R.id.ibtn_5_0);
        ibtn_5_1 = findViewById(R.id.ibtn_5_1);
        ibtn_5_2 = findViewById(R.id.ibtn_5_2);
        ibtn_5_3 = findViewById(R.id.ibtn_5_3);
        ibtn_5_4 = findViewById(R.id.ibtn_5_4);
        ibtn_5_5 = findViewById(R.id.ibtn_5_5);
        ibtn_5_6 = findViewById(R.id.ibtn_5_6);
        ibtn_5_7 = findViewById(R.id.ibtn_5_7);

//row 7
        ibtn_6_0 = findViewById(R.id.ibtn_6_0);
        ibtn_6_1 = findViewById(R.id.ibtn_6_1);
        ibtn_6_2 = findViewById(R.id.ibtn_6_2);
        ibtn_6_3 = findViewById(R.id.ibtn_6_3);
        ibtn_6_4 = findViewById(R.id.ibtn_6_4);
        ibtn_6_5 = findViewById(R.id.ibtn_6_5);
        ibtn_6_6 = findViewById(R.id.ibtn_6_6);
        ibtn_6_7 = findViewById(R.id.ibtn_6_7);

//row 7
        ibtn_7_0 = findViewById(R.id.ibtn_7_0);
        ibtn_7_1 = findViewById(R.id.ibtn_7_1);
        ibtn_7_2 = findViewById(R.id.ibtn_7_2);
        ibtn_7_3 = findViewById(R.id.ibtn_7_3);
        ibtn_7_4 = findViewById(R.id.ibtn_7_4);
        ibtn_7_5 = findViewById(R.id.ibtn_7_5);
        ibtn_7_6 = findViewById(R.id.ibtn_7_6);
        ibtn_7_7 = findViewById(R.id.ibtn_7_7);


        //row 1
        ibtn_0_0.setOnClickListener(this);
        ibtn_0_1.setOnClickListener(this);
        ibtn_0_2.setOnClickListener(this);
        ibtn_0_3.setOnClickListener(this);
        ibtn_0_4.setOnClickListener(this);
        ibtn_0_5.setOnClickListener(this);
        ibtn_0_6.setOnClickListener(this);
        ibtn_0_7.setOnClickListener(this);


//row 2
        ibtn_1_0.setOnClickListener(this);
        ibtn_1_1.setOnClickListener(this);
        ibtn_1_2.setOnClickListener(this);
        ibtn_1_3.setOnClickListener(this);
        ibtn_1_4.setOnClickListener(this);
        ibtn_1_5.setOnClickListener(this);
        ibtn_1_6.setOnClickListener(this);
        ibtn_1_7.setOnClickListener(this);
//row 3
        ibtn_2_0.setOnClickListener(this);
        ibtn_2_1.setOnClickListener(this);
        ibtn_2_2.setOnClickListener(this);
        ibtn_2_3.setOnClickListener(this);
        ibtn_2_4.setOnClickListener(this);
        ibtn_2_5.setOnClickListener(this);
        ibtn_2_6.setOnClickListener(this);
        ibtn_2_7.setOnClickListener(this);
//row 4
        ibtn_3_0.setOnClickListener(this);
        ibtn_3_1.setOnClickListener(this);
        ibtn_3_2.setOnClickListener(this);
        ibtn_3_3.setOnClickListener(this);
        ibtn_3_4.setOnClickListener(this);
        ibtn_3_5.setOnClickListener(this);
        ibtn_3_6.setOnClickListener(this);
        ibtn_3_7.setOnClickListener(this);

//row 5
        ibtn_4_0.setOnClickListener(this);
        ibtn_4_1.setOnClickListener(this);
        ibtn_4_2.setOnClickListener(this);
        ibtn_4_3.setOnClickListener(this);
        ibtn_4_4.setOnClickListener(this);
        ibtn_4_5.setOnClickListener(this);
        ibtn_4_6.setOnClickListener(this);
        ibtn_4_7.setOnClickListener(this);

//row 6
        ibtn_5_0.setOnClickListener(this);
        ibtn_5_1.setOnClickListener(this);
        ibtn_5_2.setOnClickListener(this);
        ibtn_5_3.setOnClickListener(this);
        ibtn_5_4.setOnClickListener(this);
        ibtn_5_5.setOnClickListener(this);
        ibtn_5_6.setOnClickListener(this);
        ibtn_5_7.setOnClickListener(this);

//row 7
        ibtn_6_0.setOnClickListener(this);
        ibtn_6_1.setOnClickListener(this);
        ibtn_6_2.setOnClickListener(this);
        ibtn_6_3.setOnClickListener(this);
        ibtn_6_4.setOnClickListener(this);
        ibtn_6_5.setOnClickListener(this);
        ibtn_6_6.setOnClickListener(this);
        ibtn_6_7.setOnClickListener(this);

//row 7
        ibtn_7_0.setOnClickListener(this);
        ibtn_7_1.setOnClickListener(this);
        ibtn_7_2.setOnClickListener(this);
        ibtn_7_3.setOnClickListener(this);
        ibtn_7_4.setOnClickListener(this);
        ibtn_7_5.setOnClickListener(this);
        ibtn_7_6.setOnClickListener(this);
        ibtn_7_7.setOnClickListener(this);

        imageButtonList = new ImageButton[][]
                {{ibtn_0_0, ibtn_0_1, ibtn_0_2, ibtn_0_3, ibtn_0_4, ibtn_0_5, ibtn_0_6, ibtn_0_7},
                        {ibtn_1_0, ibtn_1_1, ibtn_1_2, ibtn_1_3, ibtn_1_4, ibtn_1_5, ibtn_1_6, ibtn_1_7},
                        {ibtn_2_0, ibtn_2_1, ibtn_2_2, ibtn_2_3, ibtn_2_4, ibtn_2_5, ibtn_2_6, ibtn_2_7},
                        {ibtn_3_0, ibtn_3_1, ibtn_3_2, ibtn_3_3, ibtn_3_4, ibtn_3_5, ibtn_3_6, ibtn_3_7},
                        {ibtn_4_0, ibtn_4_1, ibtn_4_2, ibtn_4_3, ibtn_4_4, ibtn_4_5, ibtn_4_6, ibtn_4_7},
                        {ibtn_5_0, ibtn_5_1, ibtn_5_2, ibtn_5_3, ibtn_5_4, ibtn_5_5, ibtn_5_6, ibtn_5_7},
                        {ibtn_6_0, ibtn_6_1, ibtn_6_2, ibtn_6_3, ibtn_6_4, ibtn_6_5, ibtn_6_6, ibtn_6_7},
                        {ibtn_7_0, ibtn_7_1, ibtn_7_2, ibtn_7_3, ibtn_7_4, ibtn_7_5, ibtn_7_6, ibtn_7_7}};

        disableAllButton();


        room = (Room) getIntent().getSerializableExtra("room");
        tvRoom.setText(getResources().getText(R.string.room_id) + String.valueOf(room.getId()));

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        refRoom = database.getReference("ChessRoom");
        refThisRoom = refRoom.child("unavailable").child(String.valueOf(room.getId()));

        refRoom.child("unavailable").child(String.valueOf(room.getId())).setValue(room);
        refRoom.child("available").child(String.valueOf(room.getId())).removeValue();

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
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getValue(Player.class).getUsername().equals(user.getDisplayName())) {
                        player = dataSnapshot1.getValue(Player.class);
                        tvPlayer1Name.setText(getResources().getText(R.string.player1) + player.getUsername());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refThisRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (backPressed == false) {
                    if (paused == false) {
                        if (player1Exited == false) {
                            if (dataSnapshot.getValue(Room.class) != null) {
                                if (dataSnapshot.getValue(Room.class).getPlayer1() != null) {
                                    btnSurrender.setClickable(true);
                                    tvPlayer2Name.setClickable(true);
                                    tvPlayer2Name.setText(getResources().getText(R.string.player2) + dataSnapshot.getValue(Room.class).getPlayer1().getUsername());
                                    player1 = dataSnapshot.getValue(Room.class).getPlayer1();
                                    isBlacksTurn = dataSnapshot.getValue(Room.class).isBlacksTurn();
                                    round++;
                                    if (isBlacksTurn == true && (round % 2 != 0)) {
                                        Toast.makeText(BlackChessActivity.this, "Your Turn", Toast.LENGTH_SHORT).show();
                                        enableBlackButton();
                                    }
                                    board = dataSnapshot.getValue(Room.class).getChessList();
                                    processCheckerList();
                                    resetBackgroundAll();
                                    if (checkWin() == true) {
                                        player1Exited = true;
                                        Toast.makeText(BlackChessActivity.this, "You win!", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(BlackChessActivity.this);
                                        builder.setTitle("Room Update");
                                        builder.setMessage("Excellent! You win");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Nice", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                clickSound.start();
                                                player.updateWin();
                                                refSignUpPlayers.child(player.getUsername()).setValue(player);
                                                BlackChessActivity.super.onBackPressed();
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                    if (checkLose() == true) {
                                        player1Exited = true;
                                        Toast.makeText(BlackChessActivity.this, "You Lost!", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(BlackChessActivity.this);
                                        builder.setTitle("Room Update");
                                        builder.setMessage("Sorry! You Lost");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                clickSound.start();
                                                refThisRoom.removeValue();
                                                player.updateLoss();
                                                refSignUpPlayers.child(player.getUsername()).setValue(player);
                                                BlackChessActivity.super.onBackPressed();
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                } else {
                                    Toast.makeText(BlackChessActivity.this, "User exited", Toast.LENGTH_LONG).show();
                                    btnSurrender.setClickable(false);
                                    tvPlayer2Name.setClickable(false);
                                    player1Exited = true;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(BlackChessActivity.this);
                                    builder.setTitle("Room Update");
                                    builder.setMessage("Player Surrendered! You win");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            clickSound.start();
                                            player.updateWin();
                                            refSignUpPlayers.child(player.getUsername()).setValue(player);
                                            refThisRoom.removeValue();
                                            BlackChessActivity.super.onBackPressed();
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSurrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSound.start();
                AlertDialog.Builder builder = new AlertDialog.Builder(BlackChessActivity.this);
                builder.setTitle("Surrender");
                builder.setMessage("Are you sure you want to surrender?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickSound.start();
                        backPressed = true;
                        refThisRoom.child("player2").removeValue();
                        player.updateLoss();
                        refSignUpPlayers.child(player.getUsername()).setValue(player);
                        BlackChessActivity.super.onBackPressed();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickSound.start();
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
       // return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("pausedCalled", "called");
        if(backPressed == false) {
            if (player1Exited == false) {
                paused = true;
                refThisRoom.child("player2").removeValue();
                player.updateLoss();
                refSignUpPlayers.child(player.getUsername()).setValue(player);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(firstClick) {
            int id = v.getId();
            if (id == R.id.ibtn_0_0) {
                processFirstClick(0, 0);
            } else if (id == R.id.ibtn_0_1) {
                processFirstClick(0, 1);
            } else if (id == R.id.ibtn_0_2) {
                processFirstClick(0, 2);
            } else if (id == R.id.ibtn_0_3) {
                processFirstClick(0, 3);
            } else if (id == R.id.ibtn_0_4) {
                processFirstClick(0, 4);
            } else if (id == R.id.ibtn_0_5) {
                processFirstClick(0, 5);
            } else if (id == R.id.ibtn_0_6) {
                processFirstClick(0, 6);
            } else if (id == R.id.ibtn_0_7) {
                processFirstClick(0, 7);
            } else if (id == R.id.ibtn_1_0) {
                processFirstClick(1, 0);
            } else if (id == R.id.ibtn_1_1) {
                processFirstClick(1, 1);
            } else if (id == R.id.ibtn_1_2) {
                processFirstClick(1, 2);
            } else if (id == R.id.ibtn_1_3) {
                processFirstClick(1, 3);
            } else if (id == R.id.ibtn_1_4) {
                processFirstClick(1, 4);
            } else if (id == R.id.ibtn_1_5) {
                processFirstClick(1, 5);
            } else if (id == R.id.ibtn_1_6) {
                processFirstClick(1, 6);
            } else if (id == R.id.ibtn_1_7) {
                processFirstClick(1, 7);
            } else if (id == R.id.ibtn_2_0) {
                processFirstClick(2, 0);
            } else if (id == R.id.ibtn_2_1) {
                processFirstClick(2, 1);
            } else if (id == R.id.ibtn_2_2) {
                processFirstClick(2, 2);
            } else if (id == R.id.ibtn_2_3) {
                processFirstClick(2, 3);
            } else if (id == R.id.ibtn_2_4) {
                processFirstClick(2, 4);
            } else if (id == R.id.ibtn_2_5) {
                processFirstClick(2, 5);
            } else if (id == R.id.ibtn_2_6) {
                processFirstClick(2, 6);
            } else if (id == R.id.ibtn_2_7) {
                processFirstClick(2, 7);
            } else if (id == R.id.ibtn_3_0) {
                processFirstClick(3, 0);
            } else if (id == R.id.ibtn_3_1) {
                processFirstClick(3, 1);
            } else if (id == R.id.ibtn_3_2) {
                processFirstClick(3, 2);
            } else if (id == R.id.ibtn_3_3) {
                processFirstClick(3, 3);
            } else if (id == R.id.ibtn_3_4) {
                processFirstClick(3, 4);
            } else if (id == R.id.ibtn_3_5) {
                processFirstClick(3, 5);
            } else if (id == R.id.ibtn_3_6) {
                processFirstClick(3, 6);
            } else if (id == R.id.ibtn_3_7) {
                processFirstClick(3, 7);
            } else if (id == R.id.ibtn_4_0) {
                processFirstClick(4, 0);
            } else if (id == R.id.ibtn_4_1) {
                processFirstClick(4, 1);
            } else if (id == R.id.ibtn_4_2) {
                processFirstClick(4, 2);
            } else if (id == R.id.ibtn_4_3) {
                processFirstClick(4, 3);
            } else if (id == R.id.ibtn_4_4) {
                processFirstClick(4, 4);
            } else if (id == R.id.ibtn_4_5) {
                processFirstClick(4, 5);
            } else if (id == R.id.ibtn_4_6) {
                processFirstClick(4, 6);
            } else if (id == R.id.ibtn_4_7) {
                processFirstClick(4, 7);
            } else if (id == R.id.ibtn_5_0) {
                processFirstClick(5, 0);
            } else if (id == R.id.ibtn_5_1) {
                processFirstClick(5, 1);
            } else if (id == R.id.ibtn_5_2) {
                processFirstClick(5, 2);
            } else if (id == R.id.ibtn_5_3) {
                processFirstClick(5, 3);
            } else if (id == R.id.ibtn_5_4) {
                processFirstClick(5, 4);
            } else if (id == R.id.ibtn_5_5) {
                processFirstClick(5, 5);
            } else if (id == R.id.ibtn_5_6) {
                processFirstClick(5, 6);
            } else if (id == R.id.ibtn_5_7) {
                processFirstClick(5, 7);
            } else if (id == R.id.ibtn_6_0) {
                processFirstClick(6, 0);
            } else if (id == R.id.ibtn_6_1) {
                processFirstClick(6, 1);
            } else if (id == R.id.ibtn_6_2) {
                processFirstClick(6, 2);
            } else if (id == R.id.ibtn_6_3) {
                processFirstClick(6, 3);
            } else if (id == R.id.ibtn_6_4) {
                processFirstClick(6, 4);
            } else if (id == R.id.ibtn_6_5) {
                processFirstClick(6, 5);
            } else if (id == R.id.ibtn_6_6) {
                processFirstClick(6, 6);
            } else if (id == R.id.ibtn_6_7) {
                processFirstClick(6, 7);
            } else if (id == R.id.ibtn_7_0) {
                processFirstClick(7, 0);
            } else if (id == R.id.ibtn_7_1) {
                processFirstClick(7, 1);
            } else if (id == R.id.ibtn_7_2) {
                processFirstClick(7, 2);
            } else if (id == R.id.ibtn_7_3) {
                processFirstClick(7, 3);
            } else if (id == R.id.ibtn_7_4) {
                processFirstClick(7, 4);
            } else if (id == R.id.ibtn_7_5) {
                processFirstClick(7, 5);
            } else if (id == R.id.ibtn_7_6) {
                processFirstClick(7, 6);
            } else if (id == R.id.ibtn_7_7) {
                processFirstClick(7, 7);
            }

        }
        else{

            int id = v.getId();
            if (id == R.id.ibtn_0_0) {
                processSecondClick(0, 0);
            } else if (id == R.id.ibtn_0_1) {
                processSecondClick(0, 1);
            } else if (id == R.id.ibtn_0_2) {
                processSecondClick(0, 2);
            } else if (id == R.id.ibtn_0_3) {
                processSecondClick(0, 3);
            } else if (id == R.id.ibtn_0_4) {
                processSecondClick(0, 4);
            } else if (id == R.id.ibtn_0_5) {
                processSecondClick(0, 5);
            } else if (id == R.id.ibtn_0_6) {
                processSecondClick(0, 6);
            } else if (id == R.id.ibtn_0_7) {
                processSecondClick(0, 7);
            } else if (id == R.id.ibtn_1_0) {
                processSecondClick(1, 0);
            } else if (id == R.id.ibtn_1_1) {
                processSecondClick(1, 1);
            } else if (id == R.id.ibtn_1_2) {
                processSecondClick(1, 2);
            } else if (id == R.id.ibtn_1_3) {
                processSecondClick(1, 3);
            } else if (id == R.id.ibtn_1_4) {
                processSecondClick(1, 4);
            } else if (id == R.id.ibtn_1_5) {
                processSecondClick(1, 5);
            } else if (id == R.id.ibtn_1_6) {
                processSecondClick(1, 6);
            } else if (id == R.id.ibtn_1_7) {
                processSecondClick(1, 7);
            } else if (id == R.id.ibtn_2_0) {
                processSecondClick(2, 0);
            } else if (id == R.id.ibtn_2_1) {
                processSecondClick(2, 1);
            } else if (id == R.id.ibtn_2_2) {
                processSecondClick(2, 2);
            } else if (id == R.id.ibtn_2_3) {
                processSecondClick(2, 3);
            } else if (id == R.id.ibtn_2_4) {
                processSecondClick(2, 4);
            } else if (id == R.id.ibtn_2_5) {
                processSecondClick(2, 5);
            } else if (id == R.id.ibtn_2_6) {
                processSecondClick(2, 6);
            } else if (id == R.id.ibtn_2_7) {
                processSecondClick(2, 7);
            } else if (id == R.id.ibtn_3_0) {
                processSecondClick(3, 0);
            } else if (id == R.id.ibtn_3_1) {
                processSecondClick(3, 1);
            } else if (id == R.id.ibtn_3_2) {
                processSecondClick(3, 2);
            } else if (id == R.id.ibtn_3_3) {
                processSecondClick(3, 3);
            } else if (id == R.id.ibtn_3_4) {
                processSecondClick(3, 4);
            } else if (id == R.id.ibtn_3_5) {
                processSecondClick(3, 5);
            } else if (id == R.id.ibtn_3_6) {
                processSecondClick(3, 6);
            } else if (id == R.id.ibtn_3_7) {
                processSecondClick(3, 7);
            } else if (id == R.id.ibtn_4_0) {
                processSecondClick(4, 0);
            } else if (id == R.id.ibtn_4_1) {
                processSecondClick(4, 1);
            } else if (id == R.id.ibtn_4_2) {
                processSecondClick(4, 2);
            } else if (id == R.id.ibtn_4_3) {
                processSecondClick(4, 3);
            } else if (id == R.id.ibtn_4_4) {
                processSecondClick(4, 4);
            } else if (id == R.id.ibtn_4_5) {
                processSecondClick(4, 5);
            } else if (id == R.id.ibtn_4_6) {
                processSecondClick(4, 6);
            } else if (id == R.id.ibtn_4_7) {
                processSecondClick(4, 7);
            } else if (id == R.id.ibtn_5_0) {
                processSecondClick(5, 0);
            } else if (id == R.id.ibtn_5_1) {
                processSecondClick(5, 1);
            } else if (id == R.id.ibtn_5_2) {
                processSecondClick(5, 2);
            } else if (id == R.id.ibtn_5_3) {
                processSecondClick(5, 3);
            } else if (id == R.id.ibtn_5_4) {
                processSecondClick(5, 4);
            } else if (id == R.id.ibtn_5_5) {
                processSecondClick(5, 5);
            } else if (id == R.id.ibtn_5_6) {
                processSecondClick(5, 6);
            } else if (id == R.id.ibtn_5_7) {
                processSecondClick(5, 7);
            } else if (id == R.id.ibtn_6_0) {
                processSecondClick(6, 0);
            } else if (id == R.id.ibtn_6_1) {
                processSecondClick(6, 1);
            } else if (id == R.id.ibtn_6_2) {
                processSecondClick(6, 2);
            } else if (id == R.id.ibtn_6_3) {
                processSecondClick(6, 3);
            } else if (id == R.id.ibtn_6_4) {
                processSecondClick(6, 4);
            } else if (id == R.id.ibtn_6_5) {
                processSecondClick(6, 5);
            } else if (id == R.id.ibtn_6_6) {
                processSecondClick(6, 6);
            } else if (id == R.id.ibtn_6_7) {
                processSecondClick(6, 7);
            } else if (id == R.id.ibtn_7_0) {
                processSecondClick(7, 0);
            } else if (id == R.id.ibtn_7_1) {
                processSecondClick(7, 1);
            } else if (id == R.id.ibtn_7_2) {
                processSecondClick(7, 2);
            } else if (id == R.id.ibtn_7_3) {
                processSecondClick(7, 3);
            } else if (id == R.id.ibtn_7_4) {
                processSecondClick(7, 4);
            } else if (id == R.id.ibtn_7_5) {
                processSecondClick(7, 5);
            } else if (id == R.id.ibtn_7_6) {
                processSecondClick(7, 6);
            } else if (id == R.id.ibtn_7_7) {
                processSecondClick(7, 7);
            }
        }
    }


    public void resetBackgroundColor(){
        for(int r = 0; r < imageButtonList.length; r++){
            for(int c = 0; c < imageButtonList.length; c++){
                if(r%2 == 0){
                    if(c%2 == 0){
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#e1e4e9"));
                    }
                    else{
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#c9af98"));
                    }
                }
                else{
                    if(c%2 == 0){
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#c9af98"));
                    }
                    else{
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#e1e4e9"));
                    }
                }

            }
        }
    }

    public void resetBackgroundAll(){
        for(int r = 0; r < imageButtonList.length; r++){
            for(int c = 0; c < imageButtonList.length; c++){
                if(r%2 == 0){
                    if(c%2 == 0){
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#e1e4e9"));
                    }
                    else{
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#c9af98"));
                    }
                }
                else{
                    if(c%2 == 0){
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#c9af98"));
                    }
                    else{
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#e1e4e9"));
                    }
                }
                if(!(board.get(r).get(c) instanceof NullChess)) {
                    if (board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Bishop) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_bishop);
                    } else if (board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof King) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_king);
                    } else if (board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Knight) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_knight);
                    } else if (board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Pawn) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_pawn);
                    } else if (board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Queen) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_queen);
                    } else if (board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Rook) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_rook);
                    } else if (!board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Bishop) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_bishop);
                    } else if (!board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof King) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_king);
                    } else if (!board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Knight) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_knight);
                    } else if (!board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Pawn) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_pawn);
                    } else if (!board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Queen) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_queen);
                    } else if (!board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof Rook) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_rook);
                    } else {
                        return;
                    }
                }
                else{
                    imageButtonList[r][c].setImageResource(0);
                }
            }
        }
    }

    public void resetStart(){
        start.setRow(-1);
        start.setColumn(-1);
    }

    public void processFirstClick(int row, int column){
        Log.d("Clicked","clicked");
        //if no piece is in this position
        if(board.get(row).get(column) instanceof NullChess){
            Log.d("Clicked","clicked1");
            return;
        }
        //else if the the piece is the wrong color
        else if(board.get(row).get(column).isBlack() != isBlacksTurn){
            Log.d("Clicked","clicked2");
            return;
        }
        //if there is a piece and its the right color
        else{
            Log.d("Clicked","clicked3");
            Log.d("Clickedb",board.get(row).get(column).getType());
            //get all possible moves
            moves = board.get(row).get(column).getMoves(board);
            enablePossibleMoveButton();
            Log.d("Clicked",String.valueOf(moves.size()));
            //record starting position
            start = new Point(row,column);
            for(int i = 0; i < moves.size(); i++){
                imageButtonList[moves.get(i).getRow()][moves.get(i).getColumn()].setBackgroundColor(Color.WHITE);
            }
            firstClick = false;
        }
    }

    public void processSecondClick(int row, int column){
        //if the users clicks the same position twice
        //deselect the piece
        if(start.equals(new Point(row,column))){
            resetBackgroundColor();
            firstClick = true;
            disableAllButton();
            enableBlackButton();
            resetStart();
        }
        //else the users clicks other chess of the same color, possible moves, or null
        else{
            if(board.get(row).get(column) instanceof NullChess){
                for (int i = 0; i < moves.size(); i++) {
                    if (moves.get(i).equals(new Point(row, column))) {
                        board.get(row).set(column, board.get(start.getRow()).get(start.getColumn()));
                        board.get(row).get(column).setRow(row);
                        board.get(row).get(column).setColumn(column);
                        if(board.get(row).get(column) instanceof Pawn){
                            board.get(row).get(column).setFirstMove(false);
                        }
                        board.get(start.getRow()).set(start.getColumn(), nullc);
                        resetBackgroundAll();
                        firstClick = true;
                        isBlacksTurn = !isBlacksTurn;
                        refThisRoom.child("chessList").setValue(board);
                        refThisRoom.child("blacksTurn").setValue(isBlacksTurn);
                        disableAllButton();
                        resetStart();
                        break;
                    }
                }
            }
            else {
                //same color, processFirstClick
                if (board.get(row).get(column).isBlack() == isBlacksTurn) {
                    firstClick = true;
                    resetBackgroundColor();
                    processFirstClick(row, column);
                    disableAllButton();
                    enableBlackButton();
                }
                //possible moves
                else {
                    Log.d("rowColumn",String.valueOf(board.get(row).get(column).row));
                    for (int i = 0; i < moves.size(); i++) {
                        if (moves.get(i).equals(new Point(row, column))) {
                            board.get(row).set(column, board.get(start.getRow()).get(start.getColumn()));
                            board.get(row).get(column).setRow(row);
                            board.get(row).get(column).setColumn(column);
                            board.get(start.getRow()).set(start.getColumn(), nullc);
                            resetBackgroundAll();
                            firstClick = true;
                            isBlacksTurn = !isBlacksTurn;
                            refThisRoom.child("chessList").setValue(board);
                            refThisRoom.child("blacksTurn").setValue(isBlacksTurn);
                            disableAllButton();
                            resetStart();
                            break;
                        }
                    }
                }
            }

        }
    }

    public void processCheckerList() {
        Boolean isBlack;
        Boolean firstMove;
        for (int r = 0; r < board.size(); r++) {
            for (int c = 0; c < board.get(r).size(); c++) {
                if (board.get(r).get(c).getType().equals("Bishop")) {//if the checker is black
                    isBlack = board.get(r).get(c).isBlack();
                    board.get(r).set(c, (new Bishop(isBlack, board.get(r).get(c).getRow(), board.get(r).get(c).getColumn())));

                }
                if (board.get(r).get(c).getType().equals("King")) {//if the checker is black
                    isBlack = board.get(r).get(c).isBlack();
                    board.get(r).set(c, (new King(isBlack, board.get(r).get(c).getRow(), board.get(r).get(c).getColumn())));
                }
                if (board.get(r).get(c).getType().equals("Knight")) {//if the checker is black
                    isBlack = board.get(r).get(c).isBlack();
                    board.get(r).set(c, (new Knight(isBlack, board.get(r).get(c).getRow(), board.get(r).get(c).getColumn())));
                }
                if (board.get(r).get(c).getType().equals("NullChess")) {//if the checker is red
                    board.get(r).set(c, (new NullChess()));
                }
                if (board.get(r).get(c).getType().equals("Pawn")) {//if the checker is black
                    isBlack = board.get(r).get(c).isBlack();
                    firstMove =  board.get(r).get(c).isFirstMove();
                    board.get(r).set(c, (new Pawn(isBlack, board.get(r).get(c).getRow(), board.get(r).get(c).getColumn(),firstMove)));
                }
                if (board.get(r).get(c).getType().equals("Queen")) {//if the checker is black
                    isBlack = board.get(r).get(c).isBlack();
                    board.get(r).set(c, (new Queen(isBlack, board.get(r).get(c).getRow(), board.get(r).get(c).getColumn())));
                }
                if (board.get(r).get(c).getType().equals("Rook")) {//if the checker is black
                    isBlack = board.get(r).get(c).isBlack();
                    board.get(r).set(c, (new Rook(isBlack, board.get(r).get(c).getRow(), board.get(r).get(c).getColumn())));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d("Pressed","Hello");
        if(player1Exited == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Leaving while in a game");
            builder.setMessage("Leave now means surrender");
            builder.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clickSound.start();
                    backPressed = true;
                    refThisRoom.child("player2").removeValue();
                    player.updateLoss();
                    refSignUpPlayers.child(player.getUsername()).setValue(player);
                    BlackChessActivity.super.onBackPressed();
                    finish();
                }
            });
            builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clickSound.start();
                    dialog.cancel();
                }
            });
            builder.show();
        }
        else{
            backPressed = true;
            refThisRoom.removeValue();
            BlackChessActivity.super.onBackPressed();
            finish();
        }
    }

    public boolean checkWin(){
        boolean win = true;
        for(int r = 0; r < board.size(); r++){
            for(int c = 0; c < board.get(r).size(); c++){
                if(!board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof King){
                    win = false;
                    break;
                }
            }
        }
        return win;
    }

    public boolean checkLose(){
        boolean lose = true;
        for(int r = 0; r < board.size(); r++){
            for(int c = 0; c < board.get(r).size(); c++){
                if(board.get(r).get(c).isBlack() && board.get(r).get(c) instanceof King){
                    lose = false;
                    break;
                }
            }
        }
        return lose;
    }

    public void disableAllButton(){
        for (int r = 0; r < imageButtonList.length; r++) {
            for (int c = 0; c < imageButtonList[r].length; c++) {
                imageButtonList[r][c].setClickable(false);
            }
        }
    }

    public void enableBlackButton(){
        for (int r = 0; r < imageButtonList.length; r++) {
            for (int c = 0; c < imageButtonList[r].length; c++) {
                if(board.get(r).get(c).isBlack() || board.get(r).get(c) instanceof NullChess) {
                    imageButtonList[r][c].setClickable(true);
                }
            }
        }
    }

    public void enablePossibleMoveButton(){
        for(int i = 0; i < moves.size(); i++){
            Point point = moves.get(i);
            imageButtonList[point.getRow()][point.getColumn()].setClickable(true);
        }
    }




}
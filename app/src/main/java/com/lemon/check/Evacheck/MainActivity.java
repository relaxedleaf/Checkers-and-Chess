package com.lemon.check.Evacheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lemon.check.Evacheck.Activities.ForgotPasswordActivity;
import com.lemon.check.Evacheck.Activities.ReauthenticateActivity;
import com.lemon.check.Evacheck.Activities.RewardActivity;
import com.lemon.check.Evacheck.Activities.SetupActivity;
import com.lemon.check.Evacheck.Activities.TermsAndConditionsActivity;
import com.lemon.check.Evacheck.Activities.VerificationEmailActivity;
import com.lemon.check.Evacheck.CheckerPractice.CheckerPracticeActivity;
import com.lemon.check.Evacheck.Checkers.CheckerRoomActivity;
import com.lemon.check.Evacheck.Checkers.RoomManager;
import com.lemon.check.Evacheck.Chess.ChessRoomActivity;
import com.lemon.check.Evacheck.ChessPractice.Chess;


public class MainActivity extends AppCompatActivity {
    private PlayerManager playerManager = new PlayerManager();
    private RoomManager roomManager = new RoomManager();
    private Player player;

    //***Login Page***
    private EditText etEmailLogin;
    private EditText etPasswordLogin;
    private Button btnLoginXML;
    private Button ForgotPassBtn;

    //**************

    //***Sign Up Page***
    private EditText etNameSignUp;
    private EditText etEmailSignUp;
    private EditText etPasswordSignUp;
    private Button btnSignUpXML;
    //**************

    //***Status Page***
    private TextView tvWin;
    private TextView tvLoss;
    private TextView tvWinningRate;
    //**************

    MediaPlayer backgroundSound;
    MediaPlayer clickSound;
    private AlertDialog alertDialog;
    private AlertDialog.Builder dialogBuilder;

    //***MainActivity***
    private Button btnSignup;
    private Button btnLogin;
    private Button btnSignOut;
    private ImageButton ibtnCheckers;
    private ImageButton ibtnChess;
    private TextView tvGreeting;
    private TextView tvCredit;
    ImageView rwdBtn;
    TextView Cooins;
    TextView Delete_Account, textView_terms_and_Conditions;
    CheckBox checkBox_terms;
    long coins = 0;
    //*******************

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refUsername;
    FirebaseAuth mAuth;
    DatabaseReference mUserRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView btn_Emailverification;


    private static final String TAG = "DebugMainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();


        backgroundSound = MediaPlayer.create(MainActivity.this, R.raw.adventure);
        clickSound = MediaPlayer.create(MainActivity.this, R.raw.click);
        backgroundSound.setLooping(true);
        backgroundSound.stop();

        tvGreeting = findViewById(R.id.tvGreetingID);
        btnSignup = findViewById(R.id.btnSignUpID);
        btnLogin = findViewById(R.id.btnLoginID);
        btnSignOut = findViewById(R.id.btnSignOutID);
        tvCredit = findViewById(R.id.tvCredit);
        rwdBtn = findViewById(R.id.rwdBtn);
        Cooins = findViewById(R.id.Cooins);
        Delete_Account = findViewById(R.id.Delete_Account);
        textView_terms_and_Conditions = findViewById(R.id.textView_terms_and_Conditions);
        checkBox_terms = findViewById(R.id.checkBox_terms);
        btn_Emailverification=findViewById(R.id.resend_verification_email);



            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            /////
            FirebaseUser currentUser = mAuth.getCurrentUser();

            getFCMToken();
            if (currentUser != null) {
                mUserRef.getDatabase().getReference().child("Users")
                        .child(currentUser.getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Long coinsValue = snapshot.child("coins").getValue(Long.class);
                                    if (coinsValue != null) {
                                        coins = coinsValue;
                                        Cooins.setText("You have: " + coins);
                                    } else {
                                        Log.e("MainActivity", "Coins value is null");
                                    }
                                } else {
                                    Log.e("MainActivity", "User snapshot does not exist");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("MainActivity", "Database error: " + error.getMessage());
                            }
                        });

            } else {
                Toast.makeText(this, "Please Signup To Continue", Toast.LENGTH_SHORT).show();

            }

        if (user != null) {
            if (user.isEmailVerified()) {
                btn_Emailverification.setVisibility(View.GONE);
            }
        }
                btn_Emailverification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, VerificationEmailActivity.class);
                        startActivity(intent);
                    }
                });

            Delete_Account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ReauthenticateActivity.class);
                    startActivity(intent);

                }
            });
            textView_terms_and_Conditions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, TermsAndConditionsActivity.class);
                    startActivity(intent);
                }
            });


            rwdBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickSound.start();
                    startActivity(new Intent(MainActivity.this, RewardActivity.class));
                }
            });


            tvCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user!= null && user.isEmailVerified()) {
                        clickSound.start();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else
                    {
                        // Email not verified
                        Toast.makeText(MainActivity.this, "Email not Verified ", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickSound.start();
                    createSignUpDialog();
                }
            });

            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickSound.start();
                    FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mAuth.signOut();
                                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickSound.start();
                    createLoginDialog();
                }
            });

            tvGreeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickSound.start();
                    createStatusDialog();
                }
            });

            ibtnCheckers = findViewById(R.id.ibtnCheckersID);
            ibtnChess = findViewById(R.id.ibtnChessID);
            ibtnCheckers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickSound.start();
                    Intent intent = new Intent(MainActivity.this, CheckerRoomActivity.class);
                    startActivity(intent);
                }
            });

            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            refSignUpPlayers = database.getReference("Signed Up Players");
            refUsername = database.getReference("username");

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    //////verifey email
                    Log.d("Step", "Step1");
                    user = mAuth.getCurrentUser();
                    if (user != null &&  user.isEmailVerified()) {
                        if (user.getDisplayName() != null) {
                            //user is signed in
                            Log.d(TAG, "user signed in");
                            btnLogin.setVisibility(View.GONE);
                            btnSignOut.setVisibility(View.VISIBLE);
                            btnSignup.setVisibility(View.GONE);
                            tvGreeting.setText("Hello " + user.getDisplayName());
                            tvGreeting.setVisibility(View.VISIBLE);
                            ibtnCheckers.setEnabled(true);
                            ibtnChess.setEnabled(true);
                        } else {
                            //User is signed out
                            Log.d(TAG, "user signed out");
                            btnLogin.setVisibility(View.VISIBLE);
                            btnSignOut.setVisibility(View.GONE);
                            btnSignup.setVisibility(View.VISIBLE);
                            tvGreeting.setVisibility(View.GONE);
                            tvGreeting.setVisibility(View.GONE);
                            ibtnCheckers.setEnabled(false);
                            ibtnChess.setEnabled(false);
                        }

                    } else {
                        //User is signed out
                        Log.d(TAG, "user signed out");
                        btnLogin.setVisibility(View.VISIBLE);
                        btnSignOut.setVisibility(View.GONE);
                        btnSignup.setVisibility(View.VISIBLE);
                        tvGreeting.setVisibility(View.GONE);
                        ibtnCheckers.setEnabled(false);
                        ibtnChess.setEnabled(false);
                    }
                }
            };

            refUsername.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot username : dataSnapshot.getChildren()) {
                        playerManager.getUsernameList().add(username.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        private void getFCMToken() {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Get new FCM registration token
                            String token = task.getResult();

                            // Get current user ID
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();

                                // Store token in Firebase Realtime Database with user ID
                                storeTokenInDatabase(userId, token);
                            } else {
                                Log.e("FCMToken", "User is not authenticated");
                            }
                        } else {
                            // Handle the error here
                            Log.e("FCMToken", "Fetching FCM registration token failed", task.getException());
                        }
                    });
        }

        private void storeTokenInDatabase (String userId, String token){
            // Get a reference to the Firebase Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference tokensRef = database.getReference("FCMTokens");

            // Store the token under the user ID node
            tokensRef.child(userId).setValue(token)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("FCMToken", "Token stored successfully");
                        } else {
                            Log.e("FCMToken", "Failed to store token", task.getException());
                        }
                    });
        }

        @Override
        protected void onStart () {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        @Override
        protected void onStop () {
            super.onStop();

            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }

        private void createLoginDialog () {
            dialogBuilder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.login, null);
            etEmailLogin = (EditText) view.findViewById(R.id.etEmailLoginID);
            etPasswordLogin = (EditText) view.findViewById(R.id.etPasswordLoginID);
            ForgotPassBtn = (Button) view.findViewById(R.id.ForgotPassBtn);
            btnLoginXML = (Button) view.findViewById(R.id.btnLoginXMLID);
            dialogBuilder.setView(view);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
            ForgotPassBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickSound.start();
                    Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                }
            });
            btnLoginXML.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickSound.start();
                    final String email = etEmailLogin.getText().toString();
                    String password = etPasswordLogin.getText().toString();

                    if (!email.equals("") && !password.equals("")) {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Failed Sign in", Toast.LENGTH_LONG).show();
                                } else {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        if (user.isEmailVerified()) {
                                            Toast.makeText(MainActivity.this, "Signed In!", Toast.LENGTH_LONG).show();
                                            // Proceed to the main app content
                                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            sendVerificationEmail(user);
                                            Toast.makeText(MainActivity.this, "Email not verified. A verification email has been sent to " + user.getEmail(), Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        private void createSignUpDialog () {
            dialogBuilder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.signup, null);
            etNameSignUp = (EditText) view.findViewById(R.id.etNameSignupID);
            etEmailSignUp = (EditText) view.findViewById(R.id.etEmailSignupID);
            etPasswordSignUp = (EditText) view.findViewById(R.id.etPasswordSignupID);
            btnSignUpXML = (Button) view.findViewById(R.id.btnSignupXMLID);
            dialogBuilder.setView(view);
            CheckBox checkBox_terms = view.findViewById(R.id.checkBox_terms);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
            btnSignUpXML.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickSound.start();
                    final String username = etNameSignUp.getText().toString();
                    final String email = etEmailSignUp.getText().toString();
                    final String password = etPasswordSignUp.getText().toString();
                    boolean duplicateCopy = playerManager.getDuplicateUsername(username);
                    if (!checkBox_terms.isChecked()) {
                        Toast.makeText(MainActivity.this, "You must agree to the terms and conditions", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (duplicateCopy == true) {
                        Toast.makeText(MainActivity.this, "Username Taken", Toast.LENGTH_LONG).show();
                    }
                    if (!email.equals("") && !password.equals("") && !username.equals("") && duplicateCopy == false) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Failed Sign Up", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Signed Up!", Toast.LENGTH_LONG).show();
                                    //***store the sign up user info to the database***//
                                    player = playerManager.createPlayer(username, email);
                                    refSignUpPlayers.child(username).setValue(player);
                                    refUsername.child(username).setValue(username);

                                    user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username).build();
                                    // Send verification email
                                    sendVerificationEmail(user);

                                    user.updateProfile(profileUpdates);


                                    // Navigate to SetupActivity
                                    Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                                    startActivity(intent);

                                    alertDialog.dismiss();

                                    // No need to sign in again after sign up, as the user is already signed in

                                    Log.d("Step", "Step2");
                                    //Log.d("Step","Step2");
                                    //alertDialog.dismiss();

                                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(MainActivity.this, "Failed Sign in", Toast.LENGTH_LONG).show();
                                            } else {
                                                user = mAuth.getCurrentUser();
                                                Toast.makeText(MainActivity.this, "Signed In!", Toast.LENGTH_LONG).show();
                                                alertDialog.dismiss();
                                                Log.d("Step", "Step3");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });

        }

        private void sendVerificationEmail (FirebaseUser user){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email verification sent.");
                                Toast.makeText(MainActivity.this, "Email verification sent! please verify", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Failed to send verification email.", task.getException());
                                Toast.makeText(MainActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        private void createStatusDialog () {
            dialogBuilder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.status, null);
            tvWin = view.findViewById(R.id.tvWinID);
            tvLoss = view.findViewById(R.id.tvLossID);
            tvWinningRate = view.findViewById(R.id.tvWinningRateID);

            refSignUpPlayers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    player = dataSnapshot.child(user.getDisplayName()).getValue(Player.class);
                    tvWin.setText("Win: " + player.getWin());
                    tvLoss.setText("Loss: " + player.getLoss());
                    tvWinningRate.setText("Winning Rate: " + player.getWinningRate());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            dialogBuilder.setView(view);
            alertDialog = dialogBuilder.create();
            alertDialog.show();


        }

        @Override
        protected void onDestroy () {
            backgroundSound.stop();
            super.onDestroy();
        }

        public void goChess (View view){
            Intent intent = new Intent(MainActivity.this, ChessRoomActivity.class);
            startActivity(intent);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            if (item.getItemId() == R.id.checkerMenuID) {
                Intent intent = new Intent(MainActivity.this, CheckerPracticeActivity.class);
                startActivity(intent);
            }
            if (item.getItemId() == R.id.chessMenuID) {
                Intent intent = new Intent(MainActivity.this, Chess.class);
                startActivity(intent);
            }


            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return super.onCreateOptionsMenu(menu);


        }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

}
package com.lemon.check.Evacheck;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.Evacheck.Activities.ChatUsersActivity;
import com.lemon.check.Evacheck.Activities.ConnectingActivity;
import com.lemon.check.Evacheck.Activities.FindFriendActivity;
import com.lemon.check.Evacheck.Activities.FindGroupActivity;
import com.lemon.check.Evacheck.Activities.FriendRequestActivity;
import com.lemon.check.Evacheck.Activities.FriendsActivity;
import com.lemon.check.Evacheck.Activities.GroupCreateActivity;
import com.lemon.check.Evacheck.Activities.LikesActivity;
import com.lemon.check.Evacheck.Activities.ProfileActivity;
import com.lemon.check.Evacheck.Activities.RewardActivity;
import com.lemon.check.Evacheck.Activities.UserGroupsActivity;
import com.lemon.check.Evacheck.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    ActivityHomeBinding binding;
    // FirebaseAuth auth;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //FIREBASE
    FirebaseAuth mAuth;
    DatabaseReference mUserRef;
    FirebaseDatabase database;

    String profileImageUrIV, usernameV;
    CircleImageView profileImageHeader, profilePicture;
    TextView usernameHeader;
    TextView Cooins;
    CheckBox checkBox_female_connect,checkBox_male_connect;

    FirebaseUser user;
    public static final int REQUEST_CODE = 101;
    ProgressDialog mloadingBar;
    long coins=0;
    String[] permissions = new String[] {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    private int requestCode = 1;
    boolean isOkay = false;
    String gender;
    String Users;
    String selectedGender = null;
    String selectedCountry = null;





    //image picked will be same in this uri
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Spinner spinnerCountries = binding.spinnerCountries;


        // Initialize CheckBoxes for Gender Selection
        checkBox_female_connect = binding.checkBoxFemaleConnect;
        checkBox_male_connect = binding.checkBoxMaleConnect;
        checkBox_female_connect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBox_male_connect.setChecked(false);
                selectedGender = "Female";
            } else if (!checkBox_male_connect.isChecked()) {
                selectedGender = null;
            }
        });

        checkBox_male_connect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBox_female_connect.setChecked(false);
                selectedGender = "Male";
            } else if (!checkBox_female_connect.isChecked()) {
                selectedGender = null;
            }
        });

    // Initialize Spinner for country selection
        spinnerCountries = binding.spinnerCountries;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(adapter);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountry = null;
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);


        mloadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
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
                                    binding.coins.setText("You have: " + coins);
                                } else {
                                    Log.e("HomeActivity", "Coins value is null");
                                }

                                String profileImageUrl = snapshot.child("profileImage").getValue(String.class);
                                if (profileImageUrl != null && !isActivityDestroyed()) {
                                    Picasso.get()
                                            .load(profileImageUrl)
                                            .into(binding.profilePicture);
                                } else {
                                    Log.e("HomeActivity", "Profile image URL is null or activity is destroyed");
                                }
                            } else {
                                Log.e("HomeActivity", "User snapshot does not exist");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("HomeActivity", "Database error: " + error.getMessage());
                        }
                    });

        }else {
            Toast.makeText(this, "Please Signup To Continue", Toast.LENGTH_SHORT).show();

        }



        binding.findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionsGranted()) {
                    if (coins > 4) {
                        coins = coins - 5;
                        mUserRef.getDatabase().getReference().child("Users")
                                .child(currentUser.getUid())
                                .child("coins")
                                .setValue(coins);
                        Intent intent = new Intent(HomeActivity.this, ConnectingActivity.class);
                        intent.putExtra("ProfileImage", profileImageUrIV);
                       // intent.putExtra("selectedGender", selectedGender);
                       // intent.putExtra("selectedCountry", selectedCountry);
                        startActivity(intent);

                    } else {
                        Toast.makeText(HomeActivity.this, "Insufficient Coins Watch Reward Ads To Earn Coins!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    askPermissions();
                }

            }
        });
        binding.rewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RewardActivity.class));
            }
        });


        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;


        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        profileImageHeader = view.findViewById(R.id.profileImage_header);
        profilePicture = view.findViewById(R.id.profilePicture);
        usernameHeader = view.findViewById(R.id.username_header);
        Cooins=view.findViewById(R.id.Cooins);

        navigationView.setNavigationItemSelectedListener(this);


    }

    private boolean isActivityDestroyed() {
        return isFinishing() || isDestroyed();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (user==null)
        {
            Toast.makeText(this, "Please Signup To Continue", Toast.LENGTH_SHORT).show();
            finish();
        }
        else

        {
            mUserRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        profileImageUrIV=snapshot.child("profileImage").getValue().toString();
                        Long coinsValue = snapshot.child("coins").getValue(Long.class);

                        usernameV=snapshot.child("names").getValue().toString();
                        Picasso.get().load(profileImageUrIV).into(profileImageHeader);
                        usernameHeader.setText(usernameV);

                        if (coinsValue != null) {
                            coins = coinsValue;
                            Cooins.setText("Coins: " + coins);
                        } else {
                            Log.e("HomeActivity", "Coins value is null");
                        }


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeActivity.this,"Sorry! Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }



    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            Intent intent = new Intent(HomeActivity.this, FindGroupActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.profilee){
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);

        }
        if(item.getItemId() == R.id.friend){
            Intent intent = new Intent(HomeActivity.this, FriendsActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.findFriend){
            Intent intent = new Intent(HomeActivity.this, FindFriendActivity.class);
            startActivity(intent);

        }

        if(item.getItemId() == R.id.chat){
            Intent intent = new Intent(HomeActivity.this, ChatUsersActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_groupinfo){
            Intent intent = new Intent(HomeActivity.this, UserGroupsActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_create_group){
            Intent intent = new Intent(HomeActivity.this, GroupCreateActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_pending){

            Intent intent = new Intent(HomeActivity.this, FriendRequestActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_likes){

            Intent intent = new Intent(HomeActivity.this, LikesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) ;
        {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
    }

    void askPermissions(){
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    private boolean isPermissionsGranted() {
        for(String permission : permissions ){
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }



}

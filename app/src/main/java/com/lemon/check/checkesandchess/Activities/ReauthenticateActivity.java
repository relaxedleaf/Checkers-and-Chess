package com.lemon.check.checkesandchess.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.lemon.check.checkesandchess.MainActivity;
import com.lemon.check.checkesandchess.R;

import java.util.Objects;

public class ReauthenticateActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonReauthenticate, buttonDeleteAccount;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reautheticate);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonReauthenticate = findViewById(R.id.buttonReauthenticate);
        buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);

        getSupportActionBar().setTitle("Delete Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buttonReauthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reauthenticateUser();
            }
        });

        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUserAccount();
            }
        });
       // return false;
    }

    private void reauthenticateUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ReauthenticateActivity.this, "Re-authentication successful", Toast.LENGTH_SHORT).show();
                        buttonDeleteAccount.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ReauthenticateActivity.this, "Re-authentication failed: " + Objects.
                                requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void deleteUserAccount() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ReauthenticateActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                        // Delete user data from the database
                        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).removeValue();
                        // Redirect to login or main activity
                        startActivity(new Intent(ReauthenticateActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ReauthenticateActivity.this, "Account deletion failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

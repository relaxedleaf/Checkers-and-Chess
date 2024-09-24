package com.lemon.check.checkesandchess.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.lemon.check.checkesandchess.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText inputPasswordReset;
    Button btnReset;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

            inputPasswordReset=findViewById(R.id.inputPasswordReset);
            btnReset=findViewById(R.id.btnReset);
            mAuth=FirebaseAuth.getInstance();
            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email=inputPasswordReset.getText().toString();
                    if (email.isEmpty())
                    {
                        Toast.makeText(ForgotPasswordActivity.this, "Please enter your email",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                     mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful())
                             {
                                 Toast.makeText(ForgotPasswordActivity.this, "Please check your email",Toast.LENGTH_SHORT).show();
                             }
                            else
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "Email not sent !",Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                    }
                }
            });

      //  return false;
    }
}
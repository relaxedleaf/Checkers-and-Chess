package com.lemon.check.checkesandchess.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemon.check.checkesandchess.R;

import java.util.HashMap;
import java.util.Map;

public class EnterPaymentDetailsActivity extends AppCompatActivity {

    private EditText editTextPayPalEmail;
    private EditText editTextMpesaPhoneNumber;
    private Button buttonSavePaymentDetails;

    private DatabaseReference userRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        editTextPayPalEmail = findViewById(R.id.editTextPayPalEmail);
        editTextMpesaPhoneNumber = findViewById(R.id.editTextMpesaPhoneNumber);
        buttonSavePaymentDetails = findViewById(R.id.buttonSavePaymentDetails);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Groups").child(currentUserId);

        // Load existing payment details
        loadPaymentDetails();

        buttonSavePaymentDetails.setOnClickListener(v -> savePaymentDetails());
    }

    private void loadPaymentDetails() {
        userRef.child("paymentDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String paypalEmail = snapshot.child("paypalEmail").getValue(String.class);
                    String mpesaPhoneNumber = snapshot.child("mpesaPhoneNumber").getValue(String.class);

                    if (paypalEmail != null) {
                        editTextPayPalEmail.setText(paypalEmail);
                    }
                    if (mpesaPhoneNumber != null) {
                        editTextMpesaPhoneNumber.setText(mpesaPhoneNumber);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Toast.makeText(EnterPaymentDetailsActivity.this, "Failed to load payment details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePaymentDetails() {
        String paypalEmail = editTextPayPalEmail.getText().toString().trim();
        String mpesaPhoneNumber = editTextMpesaPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(paypalEmail) && TextUtils.isEmpty(mpesaPhoneNumber)) {
            Toast.makeText(this, "Please enter either PayPal email or M-Pesa phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put("paypalEmail", paypalEmail);
        paymentDetails.put("mpesaPhoneNumber", mpesaPhoneNumber);

        userRef.child("paymentDetails").setValue(paymentDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EnterPaymentDetailsActivity.this, "Payment details saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EnterPaymentDetailsActivity.this, "Failed to save payment details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

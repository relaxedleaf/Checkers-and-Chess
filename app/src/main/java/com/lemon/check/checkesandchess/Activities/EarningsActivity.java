package com.lemon.check.checkesandchess.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.lemon.check.checkesandchess.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EarningsActivity extends AppCompatActivity {

    private double totalViews;
    private Map<String, String> paymentDetails;

    private String groupId;
    private String currentUserId;
    private DatabaseReference userRef;

    // UI Elements
    private TextView groupIdTextView;
    private TextView totalViewsTextView;
    private TextView paypalEmailTextView;
    private TextView mpesaPhoneNumberTextView;
    private TextView estimatedEarningsTextView;
    private EditText viewsToWithdrawEditText;
    private CheckBox withdrawAllCheckBox;
    private Button requestPaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);

        // Initialize views
        groupIdTextView = findViewById(R.id.groupIdTextView);
        totalViewsTextView = findViewById(R.id.totalViewsTextView);
        paypalEmailTextView = findViewById(R.id.paypalEmailTextView);
        mpesaPhoneNumberTextView = findViewById(R.id.mpesaPhoneNumberTextView);
        estimatedEarningsTextView = findViewById(R.id.estimatedEarningsTextView);
        viewsToWithdrawEditText = findViewById(R.id.viewsToWithdrawEditText);
        withdrawAllCheckBox = findViewById(R.id.withdrawAllCheckBox);
        requestPaymentButton = findViewById(R.id.requestPaymentButton);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Groups").child(currentUserId);

        // Load existing payment details
        loadPaymentDetails();

        // Get the group ID from the intent or any other source
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        // Fetch group data from Firebase
        fetchGroupData(groupId);

        // Handle checkbox for "Withdraw All" logic
        withdrawAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewsToWithdrawEditText.setText(String.valueOf((int) totalViews));
                updateEstimatedEarnings(totalViews);
            } else {
                viewsToWithdrawEditText.setText("");
            }
        });

        // Handle text input changes for calculating estimated earnings
        viewsToWithdrawEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!withdrawAllCheckBox.isChecked()) {
                    try {
                        double enteredViews = Double.parseDouble(s.toString());
                        if (enteredViews <= totalViews) {
                            updateEstimatedEarnings(enteredViews);
                        } else {
                            viewsToWithdrawEditText.setError("Cannot exceed total views");
                        }
                    } catch (NumberFormatException e) {
                        estimatedEarningsTextView.setText("Estimated Earnings: $0.00");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle request payment button click
        requestPaymentButton.setOnClickListener(v -> {
            sendPaymentRequest();
        });
    }

    private void loadPaymentDetails() {
        userRef.child("paymentDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    paymentDetails = new HashMap<>();
                    String paypalEmail = snapshot.child("paypalEmail").getValue(String.class);
                    String mpesaPhoneNumber = snapshot.child("mpesaPhoneNumber").getValue(String.class);

                    if (paypalEmail != null) {
                        paypalEmailTextView.setText(paypalEmail);
                        paymentDetails.put("paypalEmail", paypalEmail);
                    } else {
                        paypalEmailTextView.setText("Not set");
                    }

                    if (mpesaPhoneNumber != null) {
                        mpesaPhoneNumberTextView.setText(mpesaPhoneNumber);
                        paymentDetails.put("mpesaPhoneNumber", mpesaPhoneNumber);
                    } else {
                        mpesaPhoneNumberTextView.setText("Not set");
                    }

                    updateUI();
                } else {
                    Toast.makeText(EarningsActivity.this, "Payment details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EarningsActivity.this, "Failed to load payment details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchGroupData(String groupId) {
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Object totalViewsObj = dataSnapshot.child("totalViews").getValue();
                    totalViews = totalViewsObj != null ? Double.parseDouble(totalViewsObj.toString()) : 0.0;
                    updateUI();
                } else {
                    Toast.makeText(EarningsActivity.this, "Group data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EarningsActivity.this, "Failed to fetch group data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        groupIdTextView.setText("Group ID: " + groupId);
        totalViewsTextView.setText("Total Views: " + totalViews);
        paypalEmailTextView.setText("PayPal Email: " + (paymentDetails.containsKey("paypalEmail") ? paymentDetails.get("paypalEmail") : "Not set"));
        mpesaPhoneNumberTextView.setText("M-Pesa Phone Number: " + (paymentDetails.containsKey("mpesaPhoneNumber") ? paymentDetails.get("mpesaPhoneNumber") : "Not set"));

        updateEstimatedEarnings(totalViews);

        requestPaymentButton.setEnabled(!paymentDetails.isEmpty());
    }

    private void updateEstimatedEarnings(double viewsToWithdraw) {
        double paymentRate = 0.05;  // Example rate per view
        double estimatedEarnings = viewsToWithdraw * paymentRate;
        estimatedEarningsTextView.setText("Estimated Earnings: $" + String.format("%.2f", estimatedEarnings));
    }

    private void sendPaymentRequest() {
        String viewsToWithdrawStr = viewsToWithdrawEditText.getText().toString();
        double viewsToWithdraw;

        if (withdrawAllCheckBox.isChecked()) {
            viewsToWithdraw = totalViews;  // Withdraw all views
        } else {
            viewsToWithdraw = Double.parseDouble(viewsToWithdrawStr);
        }

        if (viewsToWithdraw > totalViews) {
            Toast.makeText(this, "Withdraw amount exceeds available views", Toast.LENGTH_SHORT).show();
            return;
        }

        double remainingViews = totalViews - viewsToWithdraw;
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        groupRef.child("totalViews").setValue(remainingViews).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EarningsActivity.this, "Payment request initiated", Toast.LENGTH_SHORT).show();
                totalViews = remainingViews;
                updateUI();
                viewsToWithdrawEditText.setText("");
                requestPaymentButton.setEnabled(false);
            } else {
                Toast.makeText(EarningsActivity.this, "Failed to process request", Toast.LENGTH_SHORT).show();
            }
        });

        String paymentMethod = paymentDetails.containsKey("paypalEmail") ? "PayPal" : "M-Pesa";
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("groupId", groupId);
        paymentData.put("viewsToWithdraw", viewsToWithdraw);
        paymentData.put("paymentMethod", paymentMethod);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), new JSONObject(paymentData).toString());
        Request request = new Request.Builder()
                .url("https://yourserver.com/processPayment")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(EarningsActivity.this, "Failed to send payment request", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(EarningsActivity.this, "Payment processed successfully", Toast.LENGTH_SHORT).show());

                } else {
                    runOnUiThread(() -> Toast.makeText(EarningsActivity.this, "Payment request failed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}

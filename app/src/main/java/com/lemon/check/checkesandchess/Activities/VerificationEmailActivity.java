package com.lemon.check.checkesandchess.Activities;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lemon.check.checkesandchess.R;

public class VerificationEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebView webViewTerms = findViewById(R.id.verificationEmail);

        String VerifyEmail= "<h2>Resending Verification Email</h2>" +
                "<li><strong>Welcome</strong>:</li>" +
                "<p>If you have not received Verification Email do not worry, " +
                "If your account was successfuly created." +
                " Proceed, sign-in with your Email and password." +
                "The system detects Un-Verified Emails and Resends Verification Email Automatically. " +
                "Check your Email Verify and Continue" + ".</p>" +
                "<p><strong>Thank you for using Evacheck!</strong></p>";
        webViewTerms.loadData(VerifyEmail, "text/html", "UTF-8");
        webViewTerms.getSettings().setJavaScriptEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
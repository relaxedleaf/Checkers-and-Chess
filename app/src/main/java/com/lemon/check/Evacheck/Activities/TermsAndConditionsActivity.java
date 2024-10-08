package com.lemon.check.Evacheck.Activities;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.lemon.check.Evacheck.R;

public class TermsAndConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        getSupportActionBar().setTitle("Terms And Conditions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webViewTerms = findViewById(R.id.webViewTerms);
        String termsAndConditions = "<h2>Terms and Conditions</h2>" +
                "<p>Last updated: [21-07-2024]</p>" +
                "<p>Welcome to Evacheck! By using Evacheck, you agree to the following terms and conditions. Please read them carefully.</p>" +
                "<h3>1. Acceptance of Terms</h3>" +
                "<p>By accessing and using Evacheck, you accept and agree to be bound by these terms and conditions. If you do not agree with any part of these terms, you must not use the app.</p>" +
                "<h3>2. Use of the App</h3>" +
                "<ul>" +
                "<li><strong>Eligibility</strong>: You must be at least 18 years old to use Evacheck .</li>" +
                "<li><strong>Compliance</strong>: You must follow any policies made available to you within the app.</li>" +
                "<li><strong>Prohibited Uses</strong>: You agree not to misuse the app, including but not limited to hacking, spamming, or any illegal activities.</li>" +
                "</ul>" +
                "<h3>3. Your Content</h3>" +
                "<ul>" +
                "<li><strong>Ownership</strong>: You retain ownership of any intellectual property rights that you hold in the content you submit to Evacheck</li>" +
                "<li><strong>License</strong>: By submitting content, you grant Lemonvalley Studio a worldwide, royalty-free, and non-exclusive license to use, distribute, reproduce, modify, adapt, publicly perform, and publicly display such content.</li>" +
                "</ul>" +
                "<h3>4. Privacy</h3>" +
                "<p>Our privacy policy explains how we treat your personal data and protect your privacy when you use our app. By using Evacheck, you agree that we can collect and use your data according to our privacy policy.</p>" +
                "<h3>5. Modifications to Terms</h3>" +
                "<p>We may modify these terms or any additional terms that apply to the app. You should look at the terms regularly. Changes will not apply retroactively and will become effective no sooner than fourteen days after they are posted.</p>" +
                "<h3>6. Termination</h3>" +
                "<p>We may suspend or terminate your access to Evacheck if you violate these terms. Upon termination, your right to use the app will cease immediately.</p>" +
                "<h3>7. Disclaimer</h3>" +
                "<p>We provide Evacheck\"as is\" without any warranties. We do not guarantee that the app will be available at all times or that it will be free from errors or interruptions.</p>" +
                "<h3>8. Limitation of Liability</h3>" +
                "<p>To the fullest extent permitted by law, Lemonvalley Studio shall not be liable for any indirect, incidental, special, consequential, or punitive damages, or any loss of profits or revenues, whether incurred directly or indirectly.</p>" +
                "<h3>9. Governing Law</h3>" +
                "<p>These terms and conditions are governed by and construed in accordance with the laws of [Kenya/Nairobi], without regard to its conflict of law principles.</p>" +
                "<h3>10. Contact Information</h3>" +
                "<p>If you have any questions or concerns about these terms, please contact us at:</p>" +
                "<ul>" +
                "<li><strong>Email</strong>: lemonvalley4@gmail.com</li>" +
                "<li><strong>Address</strong>: Uhuru Street, 135 Muranga, Code 10200</li>" +
                "<li><strong>Phone</strong>: +254 (713) 2269-97</li>" +
                "</ul>" +
                "<p>Thank you for using Evacheck!</p>";

        webViewTerms.loadData(termsAndConditions, "text/html", "UTF-8");
        webViewTerms.getSettings().setJavaScriptEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
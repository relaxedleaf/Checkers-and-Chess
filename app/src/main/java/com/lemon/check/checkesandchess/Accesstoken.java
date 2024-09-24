package com.lemon.check.checkesandchess;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Accesstoken {

    // Firebase Cloud Messaging scope for the service account
    public static final String FIREBASE_MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";

    public static String getAccessToken() {
        try {
            // Construct the JSON service account credentials as a string
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"checki-fee94\",\n" +
                    "  \"private_key_id\": \"7824b0daf640a1af04c100c29f34a39fc311b08b\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCWlCTZE4sTwuWQ\\ndmmFoad0WBNdt5hkzv1imeu36FtjnN40GOWZmuiWUFDs9RmiV9hmj+G78750Yzra\\nRgwANNCeUYe/WN0hk+aq9moInPRUwmbaPeoua0E60dym2gYVqAA1tlvSzW+6pRT4\\nE4r7CDK05LN+P+2e4oWCzQFQo2Vbi8lUpDXLmkJQI7lvh4MjSHqwetkw0cOTdtaq\\n1K5s/TIY0TERH5t1eaW1rQqxExXhw5Zo58EOhhrUxTINUAu9S9XTtYwMdaosEMvV\\ntvUKqoh7CEZHCBt47Z13o0aKUr6iSw/5/Xb+ZdJvmg+IYOe2U+OZxZJsn7mIzS2F\\nYz69knHBAgMBAAECggEASlbbFjx6xgfLVBXRpMKZLGwrK3Jvjx1NpTc+U24oaDZS\\n0T+VYs9CkqsWONYMkMRztrbECAme9lDjBk3csOFiOTchIIubqG8fSzoaQSRTz+Cl\\n7IryqXYUHxjqqYkrxddKOzpLfPgld+z2PD9RtvtmJT5fiy2+f1lv9pKFWq1AZgAM\\nP+K7rvLWn0FwfeJQvckP/J94/puWt4j2GKFl19zYrpSlwBjVDVdpAEh5pk1lZ5m/\\n6k2WW6xmxNl/u85zZ3x4+ddlxjV4FkbdbBpw3Ne+KKAu4yKxbjPBig3rQwu/WqJN\\npWpLqeCOrdzoKDhNyNdvCr/Q4pakcqNSd/pRUrEo/QKBgQDISdH4h4+K8yn93xYV\\npZ/MmVBjNHTOZi9IshvmYsZzO43ohMAgrzQ5rssbb9Mh1erImcGT8xeTNeXgjUZD\\ny70ZOT4GIlta1YWsd911PbE04YIIrT0tLqDDS0Hg+4FiDn0kiL0tX0xPNGD3ICpd\\nPSACr/F/ooTJffeFCMuFV0K/KwKBgQDAdpX7tSagRkpqZeiSq2q2m0XK8E7lJ1UC\\nHksE8C9iBNZQSduzSD/hdmjUo9HnjYr3of12a3U4UoJkgL2t+eXk5doCdLsdmI41\\n7rk3tc1ZtHyNUFrRa1sixwVwN0bi+rKYuOmf1vFANiOEv4dZMelq5KmwxQeBRNtU\\nkz8/UmZ8wwKBgFDJzgMkkxbmHKMFkD/tDbcWhGFvsEns19fH2A8m+otSk1wd5+2H\\nHlbCFbUcdxpn0gtWUcnevTyo+e+IrLX+AWmiOE17IERUalX2B4MZ/lepOq1LS6jk\\n7P25ZnpcmIS3+Wx6J2ycywgLxa0I8W2okTfkRlJIeK1ZvTSRJp7DYu3/AoGAdhaM\\nsfMVLxPRxvSePZfFkyM0MG8/ySRX6UPS/cWtHPQg1wRqM9dpspmneojFWtCi6ovO\\n66DFPHiLnsTLHPsOjmJlYEplIKSg5QCDTmZTB+5Q2ZfrEUFQmscIVDDmYpA1krUq\\nSzNjtOVC58lob1bLhBWm9Fn/39XyyTLLhLu3PGcCgYAB+KhARb4FoPp2KuPJxKnT\\no96xkNTollYcW1oz0XOEBM6hNot9Duxul7CqEbOXt9LoN+CwmXkun13izm0uyU/R\\n6MloMew+n0dYlUDPcAMR+fbs0+WKVE7r6w1MbuCbhHpA6zVYTXjCD88aNMqkg+oa\\nhJgCdjWOlVWK1Wd/VKPxOA==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-gi4jb@checki-fee94.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"115051284888299886062\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-gi4jb%40checki-fee94.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}";

            // Create an InputStream from the JSON string
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(Lists.newArrayList(FIREBASE_MESSAGING_SCOPE));
            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();


        } catch (IOException e) {
            Log.e("Accesstoken", "Error getting access token: " + e.getMessage(), e);
            return null;
        }

    }

}

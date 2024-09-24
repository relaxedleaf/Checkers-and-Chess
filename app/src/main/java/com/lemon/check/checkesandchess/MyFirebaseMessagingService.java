package com.lemon.check.checkesandchess;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lemon.check.checkesandchess.Activities.ChatActivity;
import com.lemon.check.checkesandchess.Activities.FriendRequestActivity;
import com.lemon.check.checkesandchess.Activities.LikesActivity;
import com.lemon.check.checkesandchess.Checkers.CheckerRoomActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";
    private static final String CHANNEL_ID = "CheckersAndChessChannel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String type = remoteMessage.getData().get("type");
        String profilePictureUrl = remoteMessage.getData().get("profilePictureUrl");

        createNotificationChannel();

        Intent intent = null;
        if (type.equals("gameStart")) {
            intent = new Intent(this, CheckerRoomActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if ("message".equalsIgnoreCase(type)) {
            String userId = remoteMessage.getData().get("userId");
            intent = new Intent(this, ChatActivity.class);
            intent.putExtra("otherUserId", userId);
        } else if (type.equals("friend_request")) {
            String userId = remoteMessage.getData().get("userId");
            intent = new Intent(this, FriendRequestActivity.class);
            intent.putExtra("otherUserId", userId);
        }else if (type.equals("friend_request_accepted")) {
            String userId = remoteMessage.getData().get("userId");
            intent = new Intent(this, ChatActivity.class);
            intent.putExtra("otherUserId", userId);
        }else if (type.equals("like")) {
            String userId = remoteMessage.getData().get("userId");
            intent = new Intent(this, LikesActivity.class);
            intent.putExtra("otherUserId", userId);
        } else if ("liked_back".equalsIgnoreCase(type)) {
            String userId = remoteMessage.getData().get("userId");
            intent = new Intent(this, ChatActivity.class);
            intent.putExtra("otherUserId", userId);
        }

        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            if (profilePictureUrl != null) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    try {
                        Bitmap bitmap = getBitmapFromURL(profilePictureUrl);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.icon) // replace with your app icon
                                .setContentTitle(title)
                                .setContentText(body)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setLargeIcon(bitmap) // set profile picture as large icon
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon((Bitmap)null));

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.notify(123, builder.build());
                    } catch (Exception e) {
                        Log.e(TAG, "Error loading profile picture: ", e);
                    }
                });
            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon) // replace with your app icon
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(123, builder.build());
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Checkers and Chess Notifications";
            String description = "Channel for Checkers and Chess notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
                Log.e(TAG, "Notification Manager is null");
            }
        }
    }

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

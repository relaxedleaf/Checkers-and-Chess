package com.lemon.check.checkesandchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationManagerCompat;

public class NotificationPermissionHelper {

    private static final int REQUEST_NOTIFICATION_SETTINGS = 123;

    /**
     * Checks if notification permission is granted.
     *
     * @param context The context to use.
     * @return True if notification permission is granted, false otherwise.
     */
    public static boolean isNotificationPermissionGranted(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    /**
     * Request notification permission.
     *
     * @param activity The activity from which the permission is requested.
     */
    public static void requestNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Redirect to app settings for notification permission
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivityForResult(intent, REQUEST_NOTIFICATION_SETTINGS);
        } else {
            // Show a dialog to prompt user to enable notifications
            showEnableNotificationDialog(activity);
        }
    }

    /**
     * Show dialog to prompt user to enable notifications.
     *
     * @param context The context to use.
     */
    private static void showEnableNotificationDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enable Notifications");
        builder.setMessage("Please enable notifications for this app to receive updates.");

        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open app settings for notification permission
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

package com.lemon.check.checkesandchess.Activities;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.lemon.check.checkesandchess.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class FullScreenImageActivity extends AppCompatActivity {

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView imageView = findViewById(R.id.fullScreenImageView);
        Button downloadButton = findViewById(R.id.downloadButton);

        // Get the image URL from the intent
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");

        // Load the image using Glide
        Glide.with(this).load(imageUrl).into(imageView);

        // Set up the download button
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadImage(imageUrl);
            }
        });
       // return false;
    }

    private void DownloadImage(String imageUrl) {
        Uri uri = Uri.parse(imageUrl);

        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) || ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            String filePath = getFilePathFromContentUri(uri);
            if (filePath != null) {
                File sourceFile = new File(filePath);
                File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), sourceFile.getName());
                copyFile(sourceFile, destinationFile);
            } else {
                Toast.makeText(this, "File path not found", Toast.LENGTH_SHORT).show();
            }
        } else if ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme())) {
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setVisibleInDownloadsUi(true);
            Date date = new Date();
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, date.toString() + ".jpg");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(true);
            request.setTitle("Downloading");
            request.setDescription("Downloading Image");
            request.setMimeType("image/jpeg");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            downloadManager.enqueue(request);
        } else {
            Toast.makeText(this, "Unsupported URI scheme", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFilePathFromContentUri(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    private void copyFile(File sourceFile, File destinationFile) {
        try {
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();

            Toast.makeText(this, "Image saved to Downloads", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error copying file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

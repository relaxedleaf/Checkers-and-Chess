package com.lemon.check.checkesandchess.tools;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImageView;
import com.lemon.check.checkesandchess.R;

import java.io.OutputStream;

public class CropImageActivity extends AppCompatActivity {
    private CropImageView cropImageView;
    private Uri imageUri;
    private Button cropButton;
    private Button squareCropButton;
    private Button rectangularCropButton;
    private static final int REQUEST_CODE_CROP = 102;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        cropImageView = findViewById(R.id.cropImageView);
        cropButton = findViewById(R.id.cropButton);
        squareCropButton = findViewById(R.id.btnSquareCrop);
        rectangularCropButton = findViewById(R.id.btnRectCrop);

        String uriString = getIntent().getStringExtra("imageUri");
        if (uriString != null) {
            imageUri = Uri.parse(uriString);
            cropImageView.setImageUriAsync(imageUri);
        }

        // Set default crop shape to square
        setCropShape(CropImageView.CropShape.RECTANGLE, 1, 1);

        squareCropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CropImageActivity", "Square Crop Button Clicked");
                setCropShape(CropImageView.CropShape.RECTANGLE, 1, 1);
            }
        });

        rectangularCropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CropImageActivity", "Rectangular Crop Button Clicked");
                setCropShape(CropImageView.CropShape.RECTANGLE, 16, 9);
            }
        });

        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap croppedBitmap = cropImageView.getCroppedImage();
                if (croppedBitmap != null) {
                    Uri resultUri = saveImageToGallery(croppedBitmap);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("resultUri", resultUri);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    private void setCropShape(CropImageView.CropShape shape, int aspectRatioX, int aspectRatioY) {
        Log.d("CropImageActivity", "Setting Crop Shape: " + shape + ", Aspect Ratio: " + aspectRatioX + ":" + aspectRatioY);
        cropImageView.setCropShape(shape);
        cropImageView.setAspectRatio(aspectRatioX, aspectRatioY);
    }

    private Uri saveImageToGallery(Bitmap bitmap) {
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "cropped_image_" + System.currentTimeMillis());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        try (OutputStream outputStream = resolver.openOutputStream(uri)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }
}
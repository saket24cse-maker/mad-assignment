package com.example.question4;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView ivFullImage = findViewById(R.id.ivFullImage);
        TextView tvImageName = findViewById(R.id.tvImageName);
        TextView tvImagePath = findViewById(R.id.tvImagePath);
        TextView tvImageSize = findViewById(R.id.tvImageSize);
        TextView tvImageDate = findViewById(R.id.tvImageDate);
        Button btnDeleteImage = findViewById(R.id.btnDeleteImage);

        String uriString = getIntent().getStringExtra("image_uri");
        if (uriString == null) {
            finish();
            return;
        }

        Uri imageUri = Uri.parse(uriString);
        DocumentFile documentFile = DocumentFile.fromSingleUri(this, imageUri);

        if (documentFile != null && documentFile.exists()) {
            Glide.with(this).load(imageUri).into(ivFullImage);
            tvImageName.setText("Name: " + documentFile.getName());
            tvImagePath.setText("Path: " + imageUri.getPath());
            tvImageSize.setText("Size: " + (documentFile.length() / 1024) + " KB");

            long lastModified = documentFile.lastModified();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(lastModified));
            tvImageDate.setText("Date Taken/Modified: " + date);
        }

        btnDeleteImage.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Image")
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (documentFile != null && documentFile.delete()) {
                            Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
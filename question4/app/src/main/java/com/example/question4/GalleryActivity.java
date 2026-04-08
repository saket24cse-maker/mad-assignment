package com.example.question4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView rvGallery;
    private GalleryAdapter adapter;
    private List<DocumentFile> imageFiles = new ArrayList<>();
    private Uri folderUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        rvGallery = findViewById(R.id.rvGallery);
        TextView tvFolderPath = findViewById(R.id.tvFolderPath);

        String uriString = getIntent().getStringExtra("folder_uri");
        if (uriString != null) {
            folderUri = Uri.parse(uriString);
            tvFolderPath.setText("Folder: " + folderUri.getPath());
            loadImages();
        }

        rvGallery.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new GalleryAdapter(this, imageFiles, file -> {
            Intent intent = new Intent(GalleryActivity.this, ImageDetailActivity.class);
            intent.putExtra("image_uri", file.getUri().toString());
            startActivityForResult(intent, 1001);
        });
        rvGallery.setAdapter(adapter);
    }

    private void loadImages() {
        imageFiles.clear();
        DocumentFile root = DocumentFile.fromTreeUri(this, folderUri);
        if (root != null && root.isDirectory()) {
            for (DocumentFile file : root.listFiles()) {
                if (file.getType() != null && file.getType().startsWith("image/")) {
                    imageFiles.add(file);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // Refresh gallery if an image was deleted
            loadImages();
            adapter.notifyDataSetChanged();
        }
    }
}
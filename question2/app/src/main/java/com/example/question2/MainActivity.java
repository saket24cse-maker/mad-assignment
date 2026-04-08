package com.example.question2;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private TextView statusText;
    private Uri currentUri;
    private boolean isVideo = false;

    private final ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    currentUri = uri;
                    isVideo = getContentResolver().getType(uri).contains("video");
                    statusText.setText("Selected: " + uri.getLastPathSegment());
                    if (isVideo) {
                        setupVideo();
                    } else {
                        setupAudio();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        statusText = findViewById(R.id.statusText);

        findViewById(R.id.btnOpenFile).setOnClickListener(v -> {
            stopAll();
            filePickerLauncher.launch("audio/* video/*");
        });

        findViewById(R.id.btnOpenUrl).setOnClickListener(v -> showUrlDialog());

        findViewById(R.id.btnPlay).setOnClickListener(v -> {
            if (isVideo) {
                videoView.start();
            } else if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });

        findViewById(R.id.btnPause).setOnClickListener(v -> {
            if (isVideo) {
                videoView.pause();
            } else if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        findViewById(R.id.btnStop).setOnClickListener(v -> stopAll());

        findViewById(R.id.btnRestart).setOnClickListener(v -> {
            if (isVideo) {
                videoView.seekTo(0);
                videoView.start();
            } else if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });
    }

    private void showUrlDialog() {
        stopAll();
        EditText input = new EditText(this);
        input.setHint("Enter video URL (e.g. http://...)");
        new AlertDialog.Builder(this)
                .setTitle("Stream Video")
                .setView(input)
                .setPositiveButton("Play", (dialog, which) -> {
                    String url = input.getText().toString();
                    if (!url.isEmpty()) {
                        currentUri = Uri.parse(url);
                        isVideo = true;
                        statusText.setText("Streaming: " + url);
                        setupVideo();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupVideo() {
        stopAudio();
        videoView.setVisibility(android.view.View.VISIBLE);
        videoView.setVideoURI(currentUri);
        videoView.start();
    }

    private void setupAudio() {
        stopVideo();
        videoView.setVisibility(android.view.View.GONE);
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, currentUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Toast.makeText(this, "Error playing audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAll() {
        stopVideo();
        stopAudio();
    }

    private void stopVideo() {
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAll();
    }
}
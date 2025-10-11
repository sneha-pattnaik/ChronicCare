package com.example.chroniccare;
// VideoPlayerActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        webView = findViewById(R.id.video_web_view);

        // Retrieve the URL passed via the Intent
        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra("VIDEO_URL");

        if (videoUrl != null) {
            // Configure the WebView for video playback
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Essential for media players (e.g., YouTube)
            webSettings.setDomStorageEnabled(true);

            // IMPORTANT: This allows videos to play fullscreen and handle media events
            webView.setWebChromeClient(new WebChromeClient());

            // This client keeps the link navigation inside the WebView
            webView.setWebViewClient(new WebViewClient());

            // Load the video URL
            webView.loadUrl(videoUrl);
        } else {
            // Handle case where URL is missing (e.g., show an error message)
            finish();
        }
    }

    // Optional: Allow the back button to navigate WebView history first
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}


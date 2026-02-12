package com.example.chroniccare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chroniccare.utils.ProfileImageHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class FitHubActivity extends BottomNavActivity {
    
    private CircleImageView profileImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        profileImage = findViewById(R.id.profile_image);
        ProfileImageHelper.loadProfileImage(this, profileImage);
        profileImage.setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class))
        );
        
        findViewById(R.id.webView_gentle_yoga).setOnClickListener(v -> {
            openYouTubeVideo("EvMTrP8eRvM");
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        ProfileImageHelper.loadProfileImage(this, profileImage);
    }
    
    private void openYouTubeVideo(String videoId) {
        try {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
            startActivity(appIntent);
        } catch (Exception e) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, 
                Uri.parse("https://www.youtube.com/watch?v=" + videoId));
            startActivity(webIntent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fit_hub;
    }

    @Override
    protected int getBottomNavMenuItemId() {
        return R.id.nav_fithub;
    }
}

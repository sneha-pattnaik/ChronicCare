// MainVideoActivity.java
package com.example.chroniccare;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Replace R.layout.activity_main with your actual layout file name
        setContentView(R.layout.activity_fit_hub);

        setupVideoCards();
    }

    private void setupVideoCards() {
        // --- Video Card 1: Gentle Yoga ---
        CardView gentleYogaCard = findViewById(R.id.card_Gentle_Yoga); // Ensure this ID is in your XML
        final String gentleYogaUrl = "https://youtu.be/EvMTrP8eRvM?si=l3NhUB29vSJ6N6rR";

        gentleYogaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVideoPlayer(gentleYogaUrl);
            }
        });

        // --- Video Card 2: Surya Namaskar ---
        CardView suryaNamaskarCard = findViewById(R.id.card_Surya_Namaskar); // Ensure this ID is in your XML
        final String suryaNamaskarUrl = "https://youtu.be/UJ1L3Kpdgw0?si=FCZ3eK3YHspF1-0n";

        suryaNamaskarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVideoPlayer(suryaNamaskarUrl);
            }
        });

        // --- Video Card 3: Core Strength ---
        CardView coreStrengthCard = findViewById(R.id.card_Core_Strength); // Ensure this ID is in your XML
        final String coreStrengthUrl = "https://youtu.be/zv7kSlx7mqE?si=hO7FNXFbV_iernia";

        coreStrengthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVideoPlayer(coreStrengthUrl);
            }
        });
    }

    private void launchVideoPlayer(String videoUrl) {
        // Create an Intent to open the VideoPlayerActivity
        Intent intent = new Intent(MainVideoActivity.this, VideoPlayerActivity.class);

        // Pass the video URL as an extra
        intent.putExtra("VIDEO_URL", videoUrl);

        startActivity(intent);
    }
}
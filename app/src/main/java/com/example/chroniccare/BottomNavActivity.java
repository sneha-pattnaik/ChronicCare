package com.example.chroniccare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public abstract class BottomNavActivity extends BaseActivity {

    BottomNavigationView bottomNavigationView;

    // Child activities must provide their layout and nav ID
    protected abstract int getLayoutId();

    protected abstract int getBottomNavMenuItemId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // always load base layout
        setContentView(R.layout.activity_bottom_nav);

        // inflate child layout inside content_frame
        getLayoutInflater().inflate(getLayoutId(), findViewById(R.id.content_frame), true);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);

        // highlight the current menu item
        bottomNavigationView.setSelectedItemId(getBottomNavMenuItemId());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == getBottomNavMenuItemId()) {
                return true;
            }

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.nav_monitor) {
                startActivity(new Intent(this, MonitorActivity.class));
            } else if (itemId == R.id.nav_medications) {
                startActivity(new Intent(this, MedicationsActivity.class));
            } else if (itemId == R.id.nav_fithub) {
                startActivity(new Intent(this, FitHubActivity.class));
            } else if (itemId == R.id.nav_dr_gpt) {
                startActivity(new Intent(this, DrGPTActivity.class));
            }

            overridePendingTransition(0, 0);
            finish(); // prevent stacking
            return true;
        });
    }
}
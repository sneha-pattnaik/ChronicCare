package com.example.chroniccare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chroniccare.utils.ProfileImageHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicationsActivity extends BottomNavActivity {
    
    private CircleImageView profileImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        profileImage = findViewById(R.id.profile_image);
        ProfileImageHelper.loadProfileImage(this, profileImage);
        profileImage.setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class))
        );
        
        TextView addButton = findViewById(R.id.MedPg_add);
        if (addButton != null) {
            addButton.setOnClickListener(v -> 
                startActivity(new Intent(this, AddMedications.class))
            );
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        ProfileImageHelper.loadProfileImage(this, profileImage);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_medications;
    }

    @Override
    protected int getBottomNavMenuItemId() {
        return R.id.nav_medications;
    }
}

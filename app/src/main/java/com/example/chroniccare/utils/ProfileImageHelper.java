package com.example.chroniccare.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.chroniccare.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileImageHelper {
    private static final String TAG = "ProfileImageHelper";
    
    public static void loadProfileImage(Context context, CircleImageView imageView) {
        SharedPreferences prefs = context.getSharedPreferences("ChronicCarePrefs", Context.MODE_PRIVATE);
        String photoUrl = prefs.getString("userPhoto", "");
        
        if (!photoUrl.isEmpty()) {
            try {
                if (photoUrl.startsWith("http")) {
                    Picasso.get()
                        .load(photoUrl)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(imageView);
                } else {
                    Uri uri = Uri.parse(photoUrl);
                    imageView.setImageURI(uri);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading photo", e);
                loadGooglePhoto(context, imageView);
            }
        } else {
            loadGooglePhoto(context, imageView);
        }
    }
    
    private static void loadGooglePhoto(Context context, CircleImageView imageView) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null && account.getPhotoUrl() != null) {
            Picasso.get()
                .load(account.getPhotoUrl().toString())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_profile);
        }
    }
}

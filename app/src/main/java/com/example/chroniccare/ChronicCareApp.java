package com.example.chroniccare;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

public class ChronicCareApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences("ChronicCarePrefs", MODE_PRIVATE);
        String tag = prefs.getString("appLanguage", "en");
        if (tag != null && !tag.trim().isEmpty()) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag));
        }
    }
}

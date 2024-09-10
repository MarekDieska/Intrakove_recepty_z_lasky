package com.example.dvojplatnicka;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dvojplatnicka.R;

public class ThemeManager {

    private final SharedPreferences sharedPreferences;
    private final AppCompatActivity activity;

    public ThemeManager(AppCompatActivity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("ThemePref", Context.MODE_PRIVATE);
    }

    public void applyTheme() {
        boolean isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);
        Log.d("ThemeManager", "Applying theme: " + (isDarkTheme ? "Dark" : "Light"));
        if (isDarkTheme) {
            activity.setTheme(R.style.Theme_night);
        } else {
            activity.setTheme(R.style.Theme_day);
        }
    }

    public void toggleTheme(boolean isDark) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isDarkTheme", isDark);
        editor.apply();
    }
}

package com.example.dvojplatnicka;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

public class FontSizeManager {

    private final SharedPreferences fontSizePreferences;
    private static final String FONTPREF_NAME = "FontSizePrefs";
    private static final String FONT_SIZE_KEY = "fontSize";

    public FontSizeManager(Context context) {
        fontSizePreferences = context.getSharedPreferences(FONTPREF_NAME, Context.MODE_PRIVATE);
    }

    public void applyFontSize(TextView textView) {
        int savedFontSize = fontSizePreferences.getInt(FONT_SIZE_KEY, 28);
        textView.setTextSize(savedFontSize);
    }

    public void saveFontSize(int newSize) {
        SharedPreferences.Editor editor = fontSizePreferences.edit();
        editor.putInt(FONT_SIZE_KEY, newSize);
        editor.apply();
    }

    public int getFontSize() {
        return fontSizePreferences.getInt(FONT_SIZE_KEY, 28); // Default size if not set
    }
}


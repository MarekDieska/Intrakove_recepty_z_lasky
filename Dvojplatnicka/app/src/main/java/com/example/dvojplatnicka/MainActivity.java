package com.example.dvojplatnicka;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Button buttonBack;
    private FrameLayout[] frameLayouts;
    private TextView textView;
    private SparseArray<String> buttonTextMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Remove default title text
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Initialize the FrameLayouts and Button
        frameLayouts = new FrameLayout[]{
                findViewById(R.id.placky),
                findViewById(R.id.thai_polievka),
                findViewById(R.id.lievance),
                findViewById(R.id.chilli_con_carne),
                findViewById(R.id.muffiny),
                findViewById(R.id.baklava)
        };
        buttonBack = findViewById(R.id.buttonBack);
        textView = findViewById(R.id.startText);

        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);

        // Initialize the SparseArray with button IDs and corresponding text
        buttonTextMap = new SparseArray<>();
        buttonTextMap.put(R.id.button1, getString(R.string.placky));
        buttonTextMap.put(R.id.button2, getString(R.string.thai_polievka));
        buttonTextMap.put(R.id.button3, getString(R.string.lievance));
        buttonTextMap.put(R.id.button4, getString(R.string.chilli_con_carne));
        buttonTextMap.put(R.id.button5, getString(R.string.muffiny));
        buttonTextMap.put(R.id.button6, getString(R.string.baklava));
    }

    public void handleButtonClick(View view) {
        // Hide all FrameLayouts
        for (FrameLayout frameLayout : frameLayouts) {
            frameLayout.setVisibility(View.GONE);
        }

        // Show the "Späť" button
        buttonBack.setVisibility(View.VISIBLE);

        String buttonText = buttonTextMap.get(view.getId());
        if (buttonText != null) {
            textView.setText(buttonText);
        }
    }

    public void handleBackButtonClick(View view) {
        // Show all FrameLayouts
        for (FrameLayout frameLayout : frameLayouts) {
            frameLayout.setVisibility(View.VISIBLE);
        }

        // Hide the "Späť" button
        buttonBack.setVisibility(View.GONE);

        // Clear the TextView text
        if (textView != null) {
            textView.setText("");
        }
    }
}

package com.example.dvojplatnicka;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Button button2, buttonBack;
    private FrameLayout frameLayout1, frameLayout2;
    private TextView textView;

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

        frameLayout1 = findViewById(R.id.frameLayout1);
        frameLayout2 = findViewById(R.id.frameLayout2);
        buttonBack = findViewById(R.id.buttonBack);
        textView = findViewById(R.id.startText);

        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
    }

    public void handleButtonClick(View view) {
        // Hide the buttons at the top
        frameLayout1.setVisibility(View.GONE);
        frameLayout2.setVisibility(View.GONE);

        // Show the "Späť" button at the bottom
        buttonBack.setVisibility(View.VISIBLE);

        // Handle which button was clicked
        if (view.getId() == R.id.button1) {
            textView.setText(getString(R.string.placky));
        } else if (view.getId() == R.id.button2) {
            textView.setText(getString(R.string.thai_polievka));
        }
    }

    public void handleBackButtonClick(View view) {
        frameLayout1.setVisibility(View.VISIBLE);
        frameLayout2.setVisibility(View.VISIBLE);

        buttonBack.setVisibility(View.GONE);

        if (textView != null) {
            textView.setText("");
        }
    }
}

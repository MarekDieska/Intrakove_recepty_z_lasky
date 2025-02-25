package com.example.dvojplatnicka;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PetFragment extends Fragment {

    private ImageView petImage;
    private ImageView dirtImage;
    private Handler handler = new Handler();
    private boolean isBlinking = false;
    private static final String PREFS_NAME = "AlphaPrefs";
    private static final String ALPHA_KEY = "alpha_value";

    private int openEyes = R.drawable.penguin;
    private int closedEyes = R.drawable.penguin_blink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet, container, false);

        petImage = view.findViewById(R.id.petImage);
        dirtImage = view.findViewById(R.id.petDirt);

        loadAlpha();

        scheduleAlphaChangeWorker();
        startBlinking();

        return view;
    }

    private void loadAlpha() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float alpha = prefs.getFloat(ALPHA_KEY, -1f); // Default is -1 (to check if a value exists)
        if (alpha == -1f) {
            alpha = 0f; // Set default alpha to fully visible (1.0)
        }
        prefs.edit().putFloat(ALPHA_KEY, alpha).apply();

        dirtImage.setAlpha(alpha);
        Log.d(TAG, "Loaded Alpha Value: " + alpha);
    }


    private void scheduleAlphaChangeWorker() {
        WorkRequest alphaWorkRequest = new PeriodicWorkRequest.Builder(
                ChangeDirtWorker.class,
                15, TimeUnit.MINUTES // Runs every hour
        ).build();

        WorkManager.getInstance(requireContext()).enqueue(alphaWorkRequest);
    }

    private void startBlinking() {
        Runnable blinkRunnable = new Runnable() {
            @Override
            public void run() {
                if (isBlinking) {
                    petImage.setImageResource(openEyes);
                    Random random = new Random();
                    handler.postDelayed(this, random.nextInt(2000));
                } else {
                    petImage.setImageResource(closedEyes);
                    handler.postDelayed(this, 130);
                }
                isBlinking = !isBlinking;
            }
        };

        handler.post(blinkRunnable);
    }
}

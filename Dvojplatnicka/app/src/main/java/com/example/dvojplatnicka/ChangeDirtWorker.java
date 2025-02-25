package com.example.dvojplatnicka;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ChangeDirtWorker extends Worker {
    private static final String PREFS_NAME = "AlphaPrefs";
    private static final String ALPHA_KEY = "alpha_value";

    public ChangeDirtWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentAlpha = prefs.getFloat(ALPHA_KEY, 0f); // Load current alpha
        float newAlpha = Math.min(currentAlpha + 0.1f, 1.0f); // Increase by 0.1 (max 1.0)

        prefs.edit().putFloat(ALPHA_KEY, newAlpha).apply(); // Save new alpha value

        Log.d("ChangeDirtWorker", "Alpha increased to: " + newAlpha);

        return Result.success();
    }
}

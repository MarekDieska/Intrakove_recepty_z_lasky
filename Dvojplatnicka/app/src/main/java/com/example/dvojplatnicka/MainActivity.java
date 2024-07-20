package com.example.dvojplatnicka;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button buttonBack;
    private FrameLayout[] frameLayouts;
    private TextView textView;
    private SparseArray<String> buttonTextMap;
    private ExoPlayer player;
    private List<MediaItem> mediaItems;
    private int currentMediaIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ExoPlayer
        player = new ExoPlayer.Builder(this).build();

        // Bind the player to the view
        PlayerView playerView = findViewById(R.id.player_view);
        playerView.setPlayer(player);

        // Initialize MediaItems
        mediaItems = new ArrayList<>();
        mediaItems.add(MediaItem.fromUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.viki_hudba)));
        mediaItems.add(MediaItem.fromUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.marek_hudba)));
        mediaItems.add(MediaItem.fromUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.emka_hudba)));
        mediaItems.add(MediaItem.fromUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.marek_hudba2)));

        // Set initial media item
        player.setMediaItem(mediaItems.get(currentMediaIndex));

        // Prepare and start playback
        player.prepare();
        player.play();

        // Set up a single listener to handle playback events
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    Log.d(TAG, "Playback ended");
                    handleLoopEnd();
                } else if (playbackState == Player.STATE_READY) {
                    Log.d(TAG, "Playback ready");
                } else if (playbackState == Player.STATE_BUFFERING) {
                    Log.d(TAG, "Playback buffering");
                }
            }
        });

        // Set up the custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                findViewById(R.id.baklava),
                findViewById(R.id.hamburger),
                findViewById(R.id.carbonara),
                findViewById(R.id.shakshuka),
                findViewById(R.id.butter_chicken),
                findViewById(R.id.placky_naan),
                findViewById(R.id.palacinky),
                findViewById(R.id.pizza),
                findViewById(R.id.zapekanky),
                findViewById(R.id.crispy_chicken),
                findViewById(R.id.teriyaki),
                findViewById(R.id.tortilla_placky),
                findViewById(R.id.gyros)
        };

        // Apply rounded corners to each FrameLayout
        for (FrameLayout frameLayout : frameLayouts) {
            frameLayout.setBackgroundResource(R.drawable.rounded_corners);
        }

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
        buttonTextMap.put(R.id.button7, getString(R.string.hamburger));
        buttonTextMap.put(R.id.button8, getString(R.string.carbonara));
        buttonTextMap.put(R.id.button9, getString(R.string.shakshuka));
        buttonTextMap.put(R.id.button10, getString(R.string.butter_chicken));
        buttonTextMap.put(R.id.button11, getString(R.string.placky_naan));
        buttonTextMap.put(R.id.button12, getString(R.string.palacinky));
        buttonTextMap.put(R.id.button13, getString(R.string.pizza));
        buttonTextMap.put(R.id.button14, getString(R.string.zapekanky));
        buttonTextMap.put(R.id.button15, getString(R.string.crispy_chicken));
        buttonTextMap.put(R.id.button16, getString(R.string.teriyaki));
        buttonTextMap.put(R.id.button17, getString(R.string.tortilla_placky));
        buttonTextMap.put(R.id.button18, getString(R.string.gyros));
    }

    private void handleLoopEnd() {
        currentMediaIndex = (currentMediaIndex + 1) % mediaItems.size();
        player.setMediaItem(mediaItems.get(currentMediaIndex));
        player.prepare();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
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

package com.example.dvojplatnicka;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerManager {

    private ExoPlayer player;
    private SeekBar volumeSeekBar;
    private List<MediaItem> mediaItems;
    private int currentMediaIndex = 0;
    private SharedPreferences volumePreferences;
    private static final String PREF_NAME = "VolumePrefs";
    private static final String VOLUME_KEY = "volumeLevel";

    public MusicPlayerManager(Context context) {
        // Initialize ExoPlayer
        player = new ExoPlayer.Builder(context).build();
        PlayerView playerView = ((AppCompatActivity) context).findViewById(R.id.player_view);
        playerView.setPlayer(player);
        volumeSeekBar = ((AppCompatActivity) context).findViewById(R.id.sound_bar);

        // Set up volume preferences
        volumePreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        float savedVolume = volumePreferences.getFloat(VOLUME_KEY, 1.0f);
        player.setVolume(savedVolume);
        volumeSeekBar.setProgress((int) (savedVolume * 100));

        setupVolumeControl();
        setupMediaItems(context);
    }

    private void setupVolumeControl() {
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                player.setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float volume = seekBar.getProgress() / 100f;
                SharedPreferences.Editor editor = volumePreferences.edit();
                editor.putFloat(VOLUME_KEY, volume);
                editor.apply();
                Toast.makeText(getContext(), "Zvuk: " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Context getContext() {
        return ((AppCompatActivity) volumeSeekBar.getContext()).getApplicationContext();
    }

    private void setupMediaItems(Context context) {
        mediaItems = new ArrayList<MediaItem>();
        mediaItems.add(MediaItem.fromUri(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.viki_hudba)));
        mediaItems.add(MediaItem.fromUri(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.marek_hudba)));
        mediaItems.add(MediaItem.fromUri(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.emka_hudba)));

        player.setMediaItem(mediaItems.get(currentMediaIndex));
        player.prepare();
        player.play();

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    handleLoopEnd();
                }
            }
        });
    }

    private void handleLoopEnd() {
        currentMediaIndex = (currentMediaIndex + 1) % mediaItems.size();
        player.setMediaItem(mediaItems.get(currentMediaIndex));
        player.prepare();
    }

    public void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    public void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}


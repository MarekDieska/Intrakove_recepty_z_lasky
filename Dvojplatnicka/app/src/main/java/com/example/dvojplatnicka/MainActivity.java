package com.example.dvojplatnicka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Adapter.OnItemClickListener, Adapter.OnLikeClickListener {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_SHOPPING_LIST = "shopping_list";

    private EditText shoppingEditText;
    private Button buttonBack;
    private ImageButton settingsButton;
    private Button confirmShoppingListButton;
    private Button removeShoppingListButton;
    private TextView textView, menuText;
    private ExoPlayer player;
    private List<MediaItem> mediaItems;
    private int currentMediaIndex = 0;
    private RecyclerView recyclerView;
    private GridLayout shoppingListGrid;
    private ConstraintLayout settingsMenu;
    private RelativeLayout mainView;
    private Adapter adapter;
    private List<Item> itemList;
    private Set<String> likedRecipes;
    private boolean settings = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Switch themeSwitch;
    private ScrollView shoppingMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        likedRecipes = new HashSet<>();

        sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        boolean isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);
        if (isDarkTheme) {
            setTheme(R.style.Theme_night);
        } else {
            setTheme(R.style.Theme_day);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        likedRecipes = new HashSet<>();

        // Initialize ExoPlayer
        player = new ExoPlayer.Builder(this).build();
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

        shoppingEditText = findViewById(R.id.shopping_edit_text);
        shoppingMenu = findViewById(R.id.shopping_menu);
        themeSwitch = findViewById(R.id.theme_switch);
        mainView = findViewById(R.id.mainView);
        settingsMenu = findViewById(R.id.settings_menu);
        settingsButton = findViewById(R.id.settingsButton);
        menuText = findViewById(R.id.menuText);
        textView = findViewById(R.id.recipeText);
        buttonBack = findViewById(R.id.buttonBack);
        Button mainDishButton = findViewById(R.id.hlavne_jedla);
        Button appetizerButton = findViewById(R.id.predjedla);
        Button dessertButton = findViewById(R.id.dezerty);
        ImageButton likeButton = findViewById(R.id.likedButton);
        shoppingListGrid = findViewById(R.id.shoppingListGrid);
        confirmShoppingListButton = findViewById(R.id.confirm_button);
        removeShoppingListButton = findViewById(R.id.remove_button);
        Button shoppingListButton = findViewById(R.id.shoppingListButton);
        recyclerView = findViewById(R.id.recyclerView);

        // Set Click Listeners
        settingsButton.setOnClickListener(this::handleSettingsClick);
        buttonBack.setOnClickListener(this::handleBackButtonClick);
        mainDishButton.setOnClickListener(v -> handleCategoryButtonClick("mainDish"));
        appetizerButton.setOnClickListener(v -> handleCategoryButtonClick("appetizer"));
        dessertButton.setOnClickListener(v -> handleCategoryButtonClick("dessert"));
        likeButton.setOnClickListener(v -> handleCategoryButtonClick("liked"));
        shoppingListButton.setOnClickListener(this::shoppingListClick);
        confirmShoppingListButton.setOnClickListener(this::confirmShoppingListClick);
        removeShoppingListButton.setOnClickListener(this::removeShoppingListClick);

        themeSwitch.setChecked(isDarkTheme); // Set initial state based on current theme
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = sharedPreferences.edit();
            editor.putBoolean("isDarkTheme", isChecked);
            editor.apply();
            recreate(); // Recreate activity to apply the new theme
        });

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedText = sharedPreferences.getString(KEY_SHOPPING_LIST, ""); // Default is empty string
        shoppingEditText.setText(savedText);

        // Initialize RecyclerView
        int spanCount = 3; // Number of columns
        int spacing = getResources().getDimensionPixelSize(R.dimen.recycler_item_spacing); // Define this in your dimens.xml
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));

        // Initialize the item list and adapter
        itemList = new ArrayList<>();
        adapter = new Adapter(itemList, this, this, likedRecipes, this); // Pass the current context
        recyclerView.setAdapter(adapter);

        // Load initial category
        handleCategoryButtonClick("all");
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

        String userInput = shoppingEditText.getText().toString();

        // Save the text in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHOPPING_LIST, userInput); // Store the user input
        editor.apply();
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

    public void handleSettingsClick(View view) {
        if(!settings) {
            mainView.setVisibility(View.GONE);
            settingsMenu.setVisibility(View.VISIBLE);
            settings = true;
            shoppingListGrid.setVisibility(View.GONE);
            shoppingMenu.setVisibility(View.GONE);
        }
        else {
            mainView.setVisibility(View.VISIBLE);
            settingsMenu.setVisibility(View.GONE);
            settings = false;
            shoppingListGrid.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void handleCategoryButtonClick(String categoryTag) {
        // Clear the list only for non-"liked" categories

        AdjustList.adjust(categoryTag, itemList, likedRecipes, this);

        // Notify adapter of changes
        adapter.notifyDataSetChanged();

        // Update UI visibility
        recyclerView.setVisibility(View.VISIBLE);
        shoppingListGrid.setVisibility(View.VISIBLE);
    }



    public void handleBackButtonClick(View view) {
        // Update UI visibility
        recyclerView.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.GONE);
        findViewById(R.id.categoryGrid).setVisibility(View.VISIBLE);
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            child.setVisibility(View.VISIBLE);
        }
        textView.setText("Vitaj späť :)");
        findViewById(R.id.likedButton).setVisibility(View.VISIBLE);
        menuText.setText("Menu");
    }

    @Override
    public void onItemClick(Item item) {
        recyclerView.setVisibility(View.GONE);

        textView.setText(item.getRecipe());
        textView.setVisibility(View.VISIBLE);

        buttonBack.setVisibility(View.VISIBLE);
        findViewById(R.id.categoryGrid).setVisibility(View.GONE);
        findViewById(R.id.likedButton).setVisibility(View.GONE);
        menuText.setText(item.getText());
    }

    @Override
    public void onLikeClick(String recipeName, boolean isLiked) {
        if (isLiked) {
            likedRecipes.add(recipeName);
        } else {
            likedRecipes.remove(recipeName);
        }
        adapter.saveLikedRecipes();
    }

    public void shoppingListClick(View view) {
        confirmShoppingListButton.setVisibility(View.VISIBLE);
        removeShoppingListButton.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
        shoppingMenu.setVisibility(View.VISIBLE);
    }

    public void confirmShoppingListClick(View view) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(":)");
        mainView.setVisibility(View.VISIBLE);
        shoppingMenu.setVisibility(View.GONE);
        confirmShoppingListButton.setVisibility(View.GONE);
        removeShoppingListButton.setVisibility(View.GONE);
    }

    public void removeShoppingListClick(View view) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(":O");
        mainView.setVisibility(View.VISIBLE);
        shoppingMenu.setVisibility(View.GONE);
        confirmShoppingListButton.setVisibility(View.GONE);
        removeShoppingListButton.setVisibility(View.GONE);
        shoppingEditText.setText("");
    }
}

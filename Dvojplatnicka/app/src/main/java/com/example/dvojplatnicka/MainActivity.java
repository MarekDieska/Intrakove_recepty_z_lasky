package com.example.dvojplatnicka;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Adapter.OnItemClickListener, Adapter.OnLikeClickListener {

    // Manager classes for handling different features
    private MusicPlayerManager musicPlayerManager;
    private ThemeManager themeManager;
    private FontSizeManager fontSizeManager;
    private ShoppingListManager shoppingListManager;
    private GridLayout shoppingListGrid;
    private Button confirmButton;
    private Button shoppingListButton;
    private Button removeButton;
    private EditText shoppingEditText;

    private Button buttonBack;
    private ImageButton settingsButton;
    private TextView recipeText, menuText;
    private RecyclerView recyclerView;
    private ConstraintLayout settingsMenu;
    private RelativeLayout mainView;
    private Switch themeSwitch;
    private RelativeLayout itemMenu;
    private ScrollView shoppingMenu;
    private ImageButton likedButton;
    private NumberPicker fontSizePicker;
    private RadioGroup radioGroup;

    private List<Item> itemList = new ArrayList<>();
    private Adapter adapter;
    private Set<String> likedRecipes;
    private boolean settings = false;
    private int spanCount = 2;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = new ThemeManager(this);
        themeManager.applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        likedRecipes = new HashSet<>();

        // Initialize managers
        musicPlayerManager = new MusicPlayerManager(this);
        fontSizeManager = new FontSizeManager(this);
        shoppingListManager = new ShoppingListManager(this);

        recipeText = findViewById(R.id.recipe_text);
        fontSizePicker = findViewById(R.id.fontsize_picker);

        fontSizePicker.setMinValue(20); // Minimum font size
        fontSizePicker.setMaxValue(48); // Maximum font size
        fontSizePicker.setValue(fontSizeManager.getFontSize());

        fontSizeManager.applyFontSize(recipeText);

        // Handle NumberPicker value changes
        fontSizePicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            fontSizeManager.saveFontSize(newVal);
            recipeText.setTextSize(newVal);
        });

        initShoppingListGrid();

        // Load shopping list from SharedPreferences
        shoppingEditText.setText(shoppingListManager.loadShoppingList());

        // Initialize UI elements
        initUI();

        int spacing = 10;
        int staticMargin = 10;

        radioGroup = findViewById(R.id.radioGroup);
        spanCount = getSavedSpanCount(); // Get the saved span count

        // Set up GridLayoutManager
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // Add item decoration
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, staticMargin, true));

        // Set up Adapter
        adapter = new Adapter(itemList, this, this, likedRecipes, this, spanCount, spacing);
        recyclerView.setAdapter(adapter);

        // Handle buttons
        setupClickListeners();

        // Set up RadioGroup listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            handleSpanChange(checkedId, spacing, staticMargin);
        });
    }

    private void saveSelectedOption(int selectedOptionId) {
        SharedPreferences sharedPreferences = getSharedPreferences("SpanPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Determine spanCount based on selected option
        int spanCount;
        if (selectedOptionId == R.id.radio_option_1) {
            spanCount = 2;
        } else if (selectedOptionId == R.id.radio_option_2) {
            spanCount = 3;
        } else if (selectedOptionId == R.id.radio_option_3) {
            spanCount = 4;
        } else {
            spanCount = 2; // Default value
        }

        editor.putInt("selected_radio_option", spanCount);
        editor.apply(); // Save changes asynchronously
    }

    private int getSavedSpanCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("SpanPreferences", MODE_PRIVATE);
        return sharedPreferences.getInt("selected_radio_option", 2); // Default to 2 if not found
    }

    private void initUI() {
        // Initialize the UI elements
        settingsButton = findViewById(R.id.settingsButton);
        menuText = findViewById(R.id.menuText);
        recipeText = findViewById(R.id.recipe_text);
        buttonBack = findViewById(R.id.buttonBack);
        recyclerView = findViewById(R.id.recyclerView);
        mainView = findViewById(R.id.mainView);
        settingsMenu = findViewById(R.id.settings_menu);
        itemMenu = findViewById(R.id.item_menu);
        themeSwitch = findViewById(R.id.theme_switch);
        likedButton = findViewById(R.id.likedButton);

        themeSwitch.setChecked(getCurrentThemeState());

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            themeManager.toggleTheme(isChecked);
            recreate();
        });
    }

    private boolean getCurrentThemeState() {
        // Retrieve the current theme state from SharedPreferences
        return getSharedPreferences("ThemePref", MODE_PRIVATE)
                .getBoolean("isDarkTheme", false);
    }

    private void initShoppingListGrid() {
        radioGroup = findViewById(R.id.radioGroup);
        shoppingListGrid = findViewById(R.id.shoppingListGrid);
        shoppingListGrid.setVisibility(View.VISIBLE);
        confirmButton = findViewById(R.id.confirm_button);
        shoppingListButton = findViewById(R.id.shoppingListButton);
        removeButton = findViewById(R.id.remove_button);
        shoppingEditText = findViewById(R.id.shopping_edit_text);
        shoppingMenu = findViewById(R.id.shopping_menu);

        confirmButton.setOnClickListener(this::confirmShoppingListClick);
        shoppingListButton.setOnClickListener(this::shoppingListClick);
        removeButton.setOnClickListener(this::removeShoppingListClick);
    }

    public void confirmShoppingListClick(View view) {
        // Save shopping list to SharedPreferences
        shoppingListManager.saveShoppingList(shoppingEditText.getText().toString());
        shoppingListButton.setVisibility(View.VISIBLE);
        confirmButton.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
        shoppingMenu.setVisibility(View.GONE);
        Toast.makeText(this, "list uložený", Toast.LENGTH_SHORT).show();
    }

    public void shoppingListClick(View view) {
        // Toggle visibility of shopping list grid
        shoppingListButton.setVisibility(View.GONE);
        confirmButton.setVisibility(View.VISIBLE);
        removeButton.setVisibility(View.VISIBLE);
        shoppingEditText.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
        shoppingMenu.setVisibility(View.VISIBLE);
    }

    public void removeShoppingListClick(View view) {
        // Clear the shopping list and update UI
        shoppingListManager.clearShoppingList();
        shoppingEditText.setText("");
        shoppingListButton.setVisibility(View.VISIBLE);
        confirmButton.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
        shoppingMenu.setVisibility(View.GONE);
        Toast.makeText(this, "list vymazaný", Toast.LENGTH_SHORT).show();
    }

    private void setupClickListeners() {
        // Set up click listeners for buttons and switches
        settingsButton.setOnClickListener(this::handleSettingsClick);
        buttonBack.setOnClickListener(this::handleBackButtonClick);
        likedButton.setOnClickListener(this::handleCategoryButtonClick);

        // Switch to toggle dark/light theme
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            themeManager.toggleTheme(isChecked);
            recreate(); // Recreate the activity to apply the new theme
        });
    }

    private void handleSpanChange(int checkedId, int spacing, int staticMargin) {
        if (checkedId == R.id.radio_option_1) {
            spanCount = 2;
        } else if (checkedId == R.id.radio_option_2) {
            spanCount = 3;
        } else if (checkedId == R.id.radio_option_3) {
            spanCount = 4;
        }

        // Save the selected option
        saveSelectedOption(checkedId);

        // Update the GridLayoutManager span count
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // Optionally notify the adapter
        adapter.notifyDataSetChanged();

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, staticMargin, true));

        // Set up Adapter
        adapter = new Adapter(itemList, this, this, likedRecipes, this, spanCount, spacing);
        recyclerView.setAdapter(adapter);
    }

    public void handleSettingsClick(View view) {
        // Toggle between settings view and main view
        settings = !settings;
        mainView.setVisibility(settings ? View.GONE : View.VISIBLE);
        shoppingListGrid.setVisibility(settings ? View.GONE : View.VISIBLE);
        settingsMenu.setVisibility(settings ? View.VISIBLE : View.GONE);
        menuText.setText(settings ? "Nastavenia" : "Menu");
        if(shoppingMenu.getVisibility() == View.VISIBLE){
            shoppingMenu.setVisibility(View.GONE);
        }
        if(itemMenu.getVisibility() == View.VISIBLE){
            itemMenu.setVisibility(View.GONE);
            buttonBack.setVisibility(View.GONE);
        }
        if(confirmButton.getVisibility() == View.VISIBLE){
            confirmButton.setVisibility(View.GONE);
            shoppingListButton.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.GONE);
        }
    }

    public void handleCategoryButtonClick(View view) {
        String categoryTag = (String) view.getTag();
        if (categoryTag != null && !categoryTag.isEmpty()) {
            Toast.makeText(this, (String) view.getTag(), Toast.LENGTH_SHORT).show();
            // Adjust the item list based on the selected category tag
            AdjustList.adjust(categoryTag, itemList, likedRecipes, this);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            // Handle the case where no tag is assigned (if needed)
            Log.e("MainActivity", "No tag found for the clicked button.");
        }
    }

    public void handleBackButtonClick(View view) {
        // Handle back button click to show the main view
        buttonBack.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
        findViewById(R.id.categoryGrid).setVisibility(View.VISIBLE);
        itemMenu.setVisibility(View.GONE);
        findViewById(R.id.likedButton).setVisibility(View.VISIBLE);
        menuText.setText("Menu");
    }

    @Override
    public void onItemClick(Item item) {
        // Handle item click to show the detailed recipe
        mainView.setVisibility(View.GONE);
        recipeText.setText(item.getRecipe());
        itemMenu.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.VISIBLE);
        menuText.setText(item.getText());
    }

    @Override
    public void onLikeClick(String recipeName, boolean isLiked) {
        // Handle liking or unliking a recipe
        if (isLiked) {
            likedRecipes.add(recipeName);
            Toast.makeText(this, "\uD83D\uDC9C", Toast.LENGTH_SHORT).show();
        } else {
            likedRecipes.remove(recipeName);
        }
        adapter.saveLikedRecipes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayerManager.pausePlayer();
        shoppingListManager.saveShoppingList(shoppingEditText.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlayerManager.resumePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicPlayerManager.releasePlayer();
    }
}

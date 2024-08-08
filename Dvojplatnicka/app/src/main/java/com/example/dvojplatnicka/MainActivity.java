package com.example.dvojplatnicka;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.OnItemClickListener {

    private Button buttonBack, mainDishButton, appetizerButton, dessertButton;
    private TextView textView;
    private ExoPlayer player;
    private List<MediaItem> mediaItems;
    private int currentMediaIndex = 0;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        buttonBack = findViewById(R.id.buttonBack);
        textView = findViewById(R.id.startText);
        mainDishButton = findViewById(R.id.hlavne_jedla);
        appetizerButton = findViewById(R.id.predjedla);
        dessertButton = findViewById(R.id.dezerty);

        buttonBack.setOnClickListener(this::handleBackButtonClick);

        recyclerView = findViewById(R.id.recyclerView);

        int spanCount = 3; // Number of columns
        int spacing = getResources().getDimensionPixelSize(R.dimen.recycler_item_spacing); // Define this in your dimens.xml
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));

        // Initialize the item list and adapter
        itemList = new ArrayList<>();
        adapter = new Adapter(itemList, this);
        recyclerView.setAdapter(adapter);

        mainDishButton.setOnClickListener(v -> handleCategoryButtonClick("mainDish"));
        appetizerButton.setOnClickListener(v -> handleCategoryButtonClick("appetizer"));
        dessertButton.setOnClickListener(v -> handleCategoryButtonClick("dessert"));
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

    public void handleCategoryButtonClick(String categoryTag) {
        itemList.clear();

        switch (categoryTag) {
            case "mainDish":
                itemList.add(new Item(R.drawable.hamburger, "Hamburger", R.id.itemFrame, getString(R.string.hamburger)));
                itemList.add(new Item(R.drawable.carbonara, "Carbonara", R.id.itemFrame, getString(R.string.carbonara)));
                itemList.add(new Item(R.drawable.chilli_con_carne, "Chilli con Carne", R.id.itemFrame, getString(R.string.chilli_con_carne)));
                itemList.add(new Item(R.drawable.butter_chicken, "Butter Chicken", R.id.itemFrame, getString(R.string.butter_chicken)));
                itemList.add(new Item(R.drawable.pizza, "Pizza", R.id.itemFrame, getString(R.string.pizza)));
                itemList.add(new Item(R.drawable.crispy_chicken, "Crispy Chicken", R.id.itemFrame, getString(R.string.crispy_chicken)));
                itemList.add(new Item(R.drawable.teriyaki, "Teriyaki", R.id.itemFrame, getString(R.string.teriyaki)));
                itemList.add(new Item(R.drawable.gyros, "Gyros", R.id.itemFrame, getString(R.string.gyros)));
                break;
            case "appetizer":
                itemList.add(new Item(R.drawable.placky, "Placky", R.id.itemFrame, getString(R.string.placky)));
                itemList.add(new Item(R.drawable.th_polievka, "Thai Polievka", R.id.itemFrame, getString(R.string.thai_polievka)));
                itemList.add(new Item(R.drawable.shakshuka, "Shakshuka", R.id.itemFrame, getString(R.string.shakshuka)));
                itemList.add(new Item(R.drawable.placky_naan, "Placky naan", R.id.itemFrame, getString(R.string.placky_naan)));
                itemList.add(new Item(R.drawable.zapekanky, "Zapekanky", R.id.itemFrame, getString(R.string.zapekanky)));
                itemList.add(new Item(R.drawable.tortilla_placky, "Tortilla placky", R.id.itemFrame, getString(R.string.tortilla_placky)));
                break;
            case "dessert":
                itemList.add(new Item(R.drawable.palacinky, "Palacinky", R.id.itemFrame, getString(R.string.palacinky)));
                itemList.add(new Item(R.drawable.lievance, "Lievance", R.id.itemFrame, getString(R.string.lievance)));
                itemList.add(new Item(R.drawable.muffiny, "Muffiny", R.id.itemFrame, getString(R.string.muffiny)));
                itemList.add(new Item(R.drawable.baklava, "Baklava", R.id.itemFrame, getString(R.string.baklava)));
                break;
        }

        adapter.notifyDataSetChanged();

        // Update UI visibility
        recyclerView.setVisibility(View.VISIBLE);
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
        textView.setText("Vítaj späť :)");
    }

    @Override
    public void onItemClick(Item item) {
        // Hide all FrameLayouts
        recyclerView.setVisibility(View.GONE);

        textView.setText(item.getRecipe());
        textView.setVisibility(View.VISIBLE);

        buttonBack.setVisibility(View.VISIBLE);
        findViewById(R.id.categoryGrid).setVisibility(View.GONE);
    }

}

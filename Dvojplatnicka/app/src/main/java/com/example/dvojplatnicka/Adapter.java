package com.example.dvojplatnicka;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Item> items;
    private OnItemClickListener itemClickListener;
    private OnLikeClickListener likeClickListener;
    private Context context;
    private Set<String> likedRecipes;
    private static final String PREFS_NAME = "liked_recipes_prefs";
    private static final String LIKED_RECIPES_KEY = "liked_recipes";
    private static final String TAG = "Adapter";

    public Adapter(List<Item> items, OnItemClickListener itemClickListener, OnLikeClickListener likeClickListener, Set<String> likedRecipes, Context context) {
        this.items = items;
        this.itemClickListener = itemClickListener;
        this.likeClickListener = likeClickListener;
        this.likedRecipes = likedRecipes;
        this.context = context.getApplicationContext(); // Use application context for SharedPreferences
        if (this.context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        loadLikedRecipes(); // Load liked recipes when the Adapter is initialized
    }

    void saveLikedRecipes() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(LIKED_RECIPES_KEY, likedRecipes);
        editor.apply(); // Apply changes asynchronously

        Log.d(TAG, "Saved liked recipes: " + likedRecipes);
    }

    private void loadLikedRecipes() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> storedLikedRecipes = sharedPreferences.getStringSet(LIKED_RECIPES_KEY, new HashSet<>());
        if (storedLikedRecipes != null) {
            likedRecipes.clear(); // Clear the current set to avoid duplication
            likedRecipes.addAll(storedLikedRecipes);
        }
        Log.d(TAG, "Loaded liked recipes: " + likedRecipes);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item, itemClickListener, likeClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayoutContainer;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            frameLayoutContainer = itemView.findViewById(R.id.frameLayoutContainer);
            this.context = context;
        }

        public void bind(Item item, OnItemClickListener itemClickListener, OnLikeClickListener likeClickListener) {
            frameLayoutContainer.removeAllViews();

            View itemView = LayoutInflater.from(frameLayoutContainer.getContext())
                    .inflate(R.layout.item_frame, frameLayoutContainer, false);

            ImageButton recipeButton = itemView.findViewById(R.id.button);
            TextView textView = itemView.findViewById(R.id.textView);
            ImageButton likeButton = itemView.findViewById(R.id.like_button);

            recipeButton.setImageResource(item.getImageResId());
            textView.setText(item.getText());

            // Set the like button image based on the item's liked status
            likeButton.setImageResource(likedRecipes.contains(item.getText().trim()) ? R.drawable.liked : R.drawable.not_liked);

            // Handle the recipe button click
            recipeButton.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(item);
                }
            });

            // Handle the like button click
            likeButton.setOnClickListener(v -> {
                item.setLiked(!item.isLiked());
                if (item.isLiked()) {
                    likedRecipes.add(item.getText().trim()); // Trim the text
                    likeButton.setImageResource(R.drawable.liked);
                    Log.d(TAG, "Added to liked recipes: [" + item.getText().trim() + "]");
                } else {
                    likedRecipes.remove(item.getText().trim()); // Trim the text
                    likeButton.setImageResource(R.drawable.not_liked);
                    Log.d(TAG, "Removed from liked recipes: [" + item.getText().trim() + "]");
                }

                saveLikedRecipes(); // Save the updated liked recipes to SharedPreferences

                if (likeClickListener != null) {
                    likeClickListener.onLikeClick(item.getText(), item.isLiked());
                }
            });

            frameLayoutContainer.addView(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public interface OnLikeClickListener {
        void onLikeClick(String recipeName, boolean isLiked);
    }
}

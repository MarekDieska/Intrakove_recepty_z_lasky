package com.example.dvojplatnicka;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private int spanCount;  // Number of columns
    private int spacing;    // Spacing between items

    public Adapter(List<Item> items, OnItemClickListener itemClickListener, OnLikeClickListener likeClickListener, Set<String> likedRecipes, Context context, int spanCount, int spacing) {
        this.items = items;
        this.itemClickListener = itemClickListener;
        this.likeClickListener = likeClickListener;
        this.likedRecipes = likedRecipes;
        this.context = context.getApplicationContext(); // Use application context for SharedPreferences
        this.spanCount = spanCount;
        this.spacing = spacing;

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

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        int columnWidth = calculateItemSize();

        // Set the width and height of the FrameLayout (menu_item_button) dynamically
        ViewGroup.LayoutParams layoutParams = holder.constraintLayoutContainer.getLayoutParams();// Set height based on calculated column width
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.cardView.getLayoutParams();
        layoutParams.width = columnWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = columnWidth-50; // or any specific value
        params.height = columnWidth-50; // or any specific value;
        holder.cardView.setLayoutParams(params);
        holder.constraintLayoutContainer.setLayoutParams(layoutParams);

        // Continue binding the rest of the item
        holder.bind(item, itemClickListener, likeClickListener);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ConstraintLayout constraintLayoutContainer;
        ImageButton recipeButton;
        TextView textView;
        ImageButton likeButton;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            constraintLayoutContainer = itemView.findViewById(R.id.constraintLayoutContainer); // Ensure this ID exists
            recipeButton = itemView.findViewById(R.id.button);
            textView = itemView.findViewById(R.id.textView);
            likeButton = itemView.findViewById(R.id.like_button);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bind(Item item, OnItemClickListener itemClickListener, OnLikeClickListener likeClickListener) {
            recipeButton.setImageResource(item.getImageResId());
            textView.setText(item.getText());

            // Set the like button image based on the item's liked status
            likeButton.setImageResource(likedRecipes.contains(item.getText().trim()) ? R.drawable.like_button : R.drawable.not_like_button);

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
                    likeButton.setImageResource(R.drawable.like_button);
                    Log.d(TAG, "Added to liked recipes: [" + item.getText().trim() + "]");
                } else {
                    likedRecipes.remove(item.getText().trim()); // Trim the text
                    likeButton.setImageResource(R.drawable.not_like_button);
                    Log.d(TAG, "Removed from liked recipes: [" + item.getText().trim() + "]");
                }

                saveLikedRecipes(); // Save the updated liked recipes to SharedPreferences

                if (likeClickListener != null) {
                    likeClickListener.onLikeClick(item.getText(), item.isLiked());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public interface OnLikeClickListener {
        void onLikeClick(String recipeName, boolean isLiked);
    }

    // Method to calculate the size of each item based on the span count and spacing
    private int calculateItemSize() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;

        // Include spacing on both sides and the number of spaces between items
        int totalSpacing = spacing * (spanCount + 1);

        return (screenWidth - totalSpacing) / spanCount;
    }
}

package com.example.dvojplatnicka;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.StringJoiner;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    public static String recipe;
    private List<Item> items;
    private OnItemClickListener listener;

    public Adapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayoutContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLayoutContainer = itemView.findViewById(R.id.frameLayoutContainer);
        }

        public void bind(final Item item, final OnItemClickListener listener) {
            if (frameLayoutContainer == null) {
                throw new NullPointerException("FrameLayout with ID 'frameLayoutContainer' is null");
            }

            frameLayoutContainer.removeAllViews();

            View itemView = LayoutInflater.from(frameLayoutContainer.getContext())
                    .inflate(R.layout.item_frame, frameLayoutContainer, false);

            ImageButton button = itemView.findViewById(R.id.button);
            TextView textView = itemView.findViewById(R.id.textView);

            button.setImageResource(item.getImageResId());
            textView.setText(item.getText());
            //System.out.println(item.getString());

            // Set OnClickListener for the ImageButton
            button.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item); // This should correctly pass the item
                }
            });

            frameLayoutContainer.addView(itemView);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
}

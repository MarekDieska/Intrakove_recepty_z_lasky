package com.example.dvojplatnicka;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdjustList {
    private final String tag;

    public AdjustList(String tag, List<Item> itemList, Set<String> likedRecipes) {
        this.tag = tag;

    }

    public String getTag() {
        return tag;
    }

    public static void adjust(String tag, List<Item> itemList, Set<String> likedRecipes, Context context) {

        if (!tag.equals("obľúbené")) {
            itemList.clear();
        }

        switch (tag) {
            case "hlavné jedlá":
                itemList.add(new Item(R.drawable.hamburger, "Hamburger", R.id.button, context.getString(R.string.hamburger)));
                itemList.add(new Item(R.drawable.carbonara, "Carbonara", R.id.button, context.getString(R.string.carbonara)));
                itemList.add(new Item(R.drawable.chilli_con_carne, "Chilli con Carne", R.id.button, context.getString(R.string.chilli_con_carne)));
                itemList.add(new Item(R.drawable.butter_chicken, "Butter Chicken", R.id.button, context.getString(R.string.butter_chicken)));
                itemList.add(new Item(R.drawable.pizza, "Pizza", R.id.button, context.getString(R.string.pizza)));
                itemList.add(new Item(R.drawable.crispy_chicken, "Crispy Chicken", R.id.button, context.getString(R.string.crispy_chicken)));
                itemList.add(new Item(R.drawable.teriyaki, "Teriyaki", R.id.button, context.getString(R.string.teriyaki)));
                itemList.add(new Item(R.drawable.gyros, "Gyros", R.id.button, context.getString(R.string.gyros)));
                itemList.add(new Item(R.drawable.madarsky_gulas, "Maďarský guláš", R.id.button, context.getString(R.string.madarsky_gulas)));
                itemList.add(new Item(R.drawable.feta_cestoviny, "Feta a cestoviny", R.id.button, context.getString(R.string.feta_cestoviny)));
                itemList.add(new Item(R.drawable.spagety_s_bazalkou, "Špagety s bazalkou", R.id.button, context.getString(R.string.spagety_s_bazalkou)));
                itemList.add(new Item(R.drawable.kari_jablko, "Kari s jablkom", R.id.button, context.getString(R.string.kari_jablko)));
                itemList.add(new Item(R.drawable.segedinsky_gulas, "Segedínsky guláš", R.id.button, context.getString(R.string.segedinsky_gulas)));
                itemList.add(new Item(R.drawable.pecienka_ryza, "Pečienka s ryžou", R.id.button, context.getString(R.string.pecienka_ryza)));
                break;
            case "predjedlá":
                itemList.add(new Item(R.drawable.placky, "Placky", R.id.button, context.getString(R.string.placky)));
                itemList.add(new Item(R.drawable.th_polievka, "Thai Polievka", R.id.button, context.getString(R.string.thai_polievka)));
                itemList.add(new Item(R.drawable.shakshuka, "Shakshuka", R.id.button, context.getString(R.string.shakshuka)));
                itemList.add(new Item(R.drawable.placky_naan, "Placky naan", R.id.button, context.getString(R.string.placky_naan)));
                itemList.add(new Item(R.drawable.zapekanky, "Zapekanky", R.id.button, context.getString(R.string.zapekanky)));
                itemList.add(new Item(R.drawable.tortilla_placky, "Tortilla placky", R.id.button, context.getString(R.string.tortilla_placky)));
                break;
            case "dezerty":
                itemList.add(new Item(R.drawable.palacinky, "Palacinky", R.id.button, context.getString(R.string.palacinky)));
                itemList.add(new Item(R.drawable.lievance, "Lievance", R.id.button, context.getString(R.string.lievance)));
                itemList.add(new Item(R.drawable.muffiny, "Muffiny", R.id.button, context.getString(R.string.muffiny)));
                itemList.add(new Item(R.drawable.baklava, "Baklava", R.id.button, context.getString(R.string.baklava)));
                itemList.add(new Item(R.drawable.tiramisu, "Tiramisu", R.id.button, context.getString(R.string.tiramisu)));
                itemList.add(new Item(R.drawable.moravske_kolace, "Moravské koláče", R.id.button, context.getString(R.string.moravske_kolace)));
                break;
            case "obľúbené":
                // Create a new list for liked items
                List<Item> likedItems = new ArrayList<>();
                for (Item item : itemList) {
                    if (likedRecipes.contains(item.getText().trim())) {
                        likedItems.add(item);
                    }
                }
                // Clear the original list and add only liked items
                itemList.clear();
                itemList.addAll(likedItems);
                break;
            default:
                break;
        }
    }
}

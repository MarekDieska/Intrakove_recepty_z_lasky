package com.example.dvojplatnicka;

import android.content.Context;
import android.content.SharedPreferences;

public class ShoppingListManager {

    private final SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_SHOPPING_LIST = "shopping_list";

    public ShoppingListManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveShoppingList(String shoppingList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHOPPING_LIST, shoppingList);
        editor.apply();
    }

    public String loadShoppingList() {
        return sharedPreferences.getString(KEY_SHOPPING_LIST, "");
    }

    public void clearShoppingList() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_SHOPPING_LIST);
        editor.apply();
    }
}
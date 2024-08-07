package com.example.dvojplatnicka;

public class Item {
    private int imageResId;
    private String text;
    private int id;  // Optional: ID or any other identifier
    private static String string;

    // Constructor
    public Item(int imageResId, String text, int id, String string) {
        this.imageResId = imageResId;
        this.text = text;
        this.id = id;
        this.string = string;
    }

    // Getter and Setter for imageResId
    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    // Getter and Setter for text
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}

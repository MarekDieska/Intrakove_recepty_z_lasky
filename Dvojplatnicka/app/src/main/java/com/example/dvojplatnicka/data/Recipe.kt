package com.example.dvojplatnicka.data

import androidx.annotation.StringDef
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RecipeType {
    MAIN_COURSE,
    STARTER,
    DESSERT
}

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val content: String,
    val image: Int? = null,
    val liked: Boolean = false,
    val type: RecipeType
)
package com.example.dvojplatnicka.data

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> = value.split(",")

    @TypeConverter
    fun fromRecipeType(value: RecipeType): String = value.name

    @TypeConverter
    fun toRecipeType(value: String): RecipeType = RecipeType.valueOf(value)
}

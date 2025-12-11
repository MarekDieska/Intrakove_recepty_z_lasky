package com.example.dvojplatnicka.data

import androidx.lifecycle.LiveData

class RecipeRepository(private val recipeDao: RecipeDao) {

    fun getAllRecipes(): LiveData<List<Recipe>> = recipeDao.getAllRecipes()

    suspend fun getRecipeById(id: Long): Recipe? {
        return recipeDao.getRecipeById(id)
    }

    suspend fun searchRecipes(query: String): List<Recipe> {
        return recipeDao.searchRecipes(query)
    }

    suspend fun insertRecipe(recipe: Recipe): Long {
        return recipeDao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }
}

package com.example.dvojplatnicka

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dvojplatnicka.data.Recipe

class Adapter(
    private var recipeList: List<Recipe>,
    private val listener: OnRecipeClickListener
) : RecyclerView.Adapter<Adapter.RecipeViewHolder>() {

    interface OnRecipeClickListener {
        fun onEdit(recipe: Recipe)
        fun onDelete(recipe: Recipe)
    }

    fun setRecipes(recipes: List<Recipe>) {
        this.recipeList = recipes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false) // Use grid-friendly layout here
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]

        holder.imageView.setOnClickListener {
            val fragment = RecipeDetailFragment.newInstance(recipe)
            val activity = holder.itemView.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        holder.nameText.text = recipe.name
        holder.imageView.setImageResource(recipe.image ?: R.drawable.dvojplatnicka_logo)
    }

    override fun getItemCount(): Int = recipeList.size

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.recipe_image)
        val nameText: TextView = itemView.findViewById(R.id.recipe_name)
    }
}

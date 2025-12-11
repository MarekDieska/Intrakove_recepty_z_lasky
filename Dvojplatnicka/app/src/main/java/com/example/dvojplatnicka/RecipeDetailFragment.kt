package com.example.dvojplatnicka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dvojplatnicka.data.Recipe

class RecipeDetailFragment : Fragment() {

    companion object {
        private const val ARG_RECIPE_NAME = "recipe_name"
        private const val ARG_RECIPE_CONTENT = "recipe_content"
        private const val ARG_RECIPE_IMAGE = "recipe_image"

        fun newInstance(recipe: Recipe): RecipeDetailFragment {
            val fragment = RecipeDetailFragment()
            val args = Bundle()
            args.putString(ARG_RECIPE_NAME, recipe.name)
            args.putString(ARG_RECIPE_CONTENT, recipe.content)
            args.putInt(ARG_RECIPE_IMAGE, recipe.image ?: 0)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeName = arguments?.getString(ARG_RECIPE_NAME)
        val recipeContent = arguments?.getString(ARG_RECIPE_CONTENT)
        val recipeImage = arguments?.getInt(ARG_RECIPE_IMAGE)

        view.findViewById<TextView>(R.id.detail_recipe_name).text = recipeName
        view.findViewById<TextView>(R.id.detail_recipe_content).text = recipeContent
        view.findViewById<ImageView>(R.id.detail_recipe_image)
            .setImageResource(recipeImage ?: R.drawable.dvojplatnicka_logo)
    }
}

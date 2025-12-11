package com.example.dvojplatnicka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.dvojplatnicka.data.Recipe
import com.example.dvojplatnicka.data.RecipeDao
import com.example.dvojplatnicka.data.RecipeDatabase
import com.example.dvojplatnicka.data.RecipeType
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    private lateinit var adapter: Adapter
    private lateinit var recipeDao: RecipeDao
    private var allRecipes: List<Recipe> = emptyList()

    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabStarter: FloatingActionButton
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabDessert: FloatingActionButton
    // New: Clear filter FAB
    private lateinit var fabClear: FloatingActionButton

    private var fabExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val db = RecipeDatabase.getInstance(requireContext())
        recipeDao = db.recipeDao()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        adapter = Adapter(ArrayList(), object : Adapter.OnRecipeClickListener {
            override fun onEdit(recipe: Recipe) { }
            override fun onDelete(recipe: Recipe) {
                lifecycleScope.launch { recipeDao.deleteRecipe(recipe) }
            }
        })
        recyclerView.adapter = adapter

        // Initialize all FABs
        fabAdd = view.findViewById(R.id.fab_add)
        fabStarter = view.findViewById(R.id.fab_starter)
        fabMain = view.findViewById(R.id.fab_main_course)
        fabDessert = view.findViewById(R.id.fab_dessert)
        fabClear = view.findViewById(R.id.fab_clear)

        // Set click listener for the main FAB
        fabAdd.setOnClickListener {
            toggleFabMenu()
        }

        // Set click listeners for the mini-FABs
        fabStarter.setOnClickListener {
            filterByType(RecipeType.STARTER)
            toggleFabMenu()
        }

        fabMain.setOnClickListener {
            filterByType(RecipeType.MAIN_COURSE)
            toggleFabMenu()
        }

        fabDessert.setOnClickListener {
            filterByType(RecipeType.DESSERT)
            toggleFabMenu()
        }

        // New: Clear filter FAB click listener
        fabClear.setOnClickListener {
            // Reset the RecyclerView to show all recipes
            adapter.setRecipes(allRecipes)
            toggleFabMenu() // Close the menu after clearing the filter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeDao.getAllRecipes().observe(viewLifecycleOwner) { recipes ->
            allRecipes = recipes
            adapter.setRecipes(recipes)
        }

        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterRecipes(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterRecipes(newText)
                return true
            }
        })
    }

    private fun toggleFabMenu() {
        if (fabExpanded) {
            // Collapse the menu
            fabStarter.hide()
            fabMain.hide()
            fabDessert.hide()
            fabClear.hide() // Also hide the new clear filter FAB
            fabAdd.setImageResource(R.drawable.ic_menu)
        } else {
            // Expand the menu
            fabStarter.show()
            fabMain.show()
            fabDessert.show()
            fabClear.show() // Also show the new clear filter FAB
            fabAdd.setImageResource(R.drawable.ic_close)
        }
        fabExpanded = !fabExpanded
    }

    private fun filterRecipes(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.setRecipes(allRecipes)
        } else {
            val words = query.trim().split("\\s+".toRegex())
            val filtered = allRecipes.filter { recipe ->
                words.all { word ->
                    recipe.name.contains(word, ignoreCase = true) ||
                            recipe.content.contains(word, ignoreCase = true)
                }
            }
            adapter.setRecipes(filtered)
        }
    }

    private fun filterByType(type: RecipeType) {
        val filtered = allRecipes.filter { it.type == type }
        adapter.setRecipes(filtered)
    }
}
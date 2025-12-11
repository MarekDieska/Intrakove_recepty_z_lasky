package com.example.dvojplatnicka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dvojplatnicka.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add MainFragment only if it’s not already added
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }
    }
}

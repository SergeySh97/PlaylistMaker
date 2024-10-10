package com.google.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.playlistmaker.databinding.ActivityMainBinding
import com.google.playlistmaker.media.ui.activity.MediaActivity
import com.google.playlistmaker.search.ui.activity.SearchActivity
import com.google.playlistmaker.settings.ui.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        initializeUI()
        setContentView(binding.root)
    }

    private fun initializeUI() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(binding) {
            btSearch.setOnClickListener {
                startActivity(Intent(applicationContext, SearchActivity::class.java))
            }
            btMedia.setOnClickListener {
                startActivity(Intent(applicationContext, MediaActivity::class.java))
            }
            btSettings.setOnClickListener {
                startActivity(Intent(applicationContext, SettingsActivity::class.java))
            }
        }
    }
}
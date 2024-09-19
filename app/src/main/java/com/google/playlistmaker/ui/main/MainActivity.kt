package com.google.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.playlistmaker.databinding.ActivityMainBinding
import com.google.playlistmaker.presentation.thememanager.SwitchTheme
import com.google.playlistmaker.ui.media.MediaActivity
import com.google.playlistmaker.ui.search.SearchActivity
import com.google.playlistmaker.ui.settings.SettingsActivity

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
        val switchTheme = SwitchTheme(this)
        switchTheme.switchTheme(switchTheme.isNightMode())
    }
}
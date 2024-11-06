package com.google.playlistmaker.media.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.ActivityMediaBinding
import com.google.playlistmaker.media.ui.MediaViewPagerAdapter

class MediaActivity : AppCompatActivity() {

    private val binding: ActivityMediaBinding by lazy {
        ActivityMediaBinding.inflate(layoutInflater)
    }
    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initializeUI()
    }
    private fun initializeUI() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btBack.setOnClickListener{
            finish()
        }
        val favoriteList = intent.getStringExtra(FAVORITE_LIST) ?: ""
        val playlist = intent.getStringExtra(PLAYLIST) ?: ""
        binding.viewPager.adapter = MediaViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
            favoriteList = favoriteList,
            playlist = playlist)

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabLayoutMediator?.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator?.detach()
    }

    private companion object {
        const val FAVORITE_LIST = "favorite_list"
        const val PLAYLIST = "playlist"
    }
}
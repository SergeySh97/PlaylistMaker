package com.google.playlistmaker.media.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.playlistmaker.media.ui.fragment.FavoriteFragment
import com.google.playlistmaker.media.ui.fragment.PlaylistFragment

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val favoriteList: String,
    private val playlist: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteFragment.newInstance(favoriteList)
            else -> PlaylistFragment.newInstance(playlist)
        }
    }
}
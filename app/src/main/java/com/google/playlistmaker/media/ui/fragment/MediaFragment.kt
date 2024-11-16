package com.google.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.FragmentMediaBinding
import com.google.playlistmaker.media.ui.MediaViewPagerAdapter

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding: FragmentMediaBinding get() = requireNotNull(_binding) { "Binding wasn't initialized!" }

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator?.detach()
        _binding = null
    }

    private fun initializeUI() {
        val favoriteList = FAVORITE_LIST
        val playlist = PLAYLIST
        binding.viewPager.adapter = MediaViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            favoriteList = favoriteList,
            playlist = playlist
        )

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.favorite)
                else -> getString(R.string.playlists)
            }
        }
        tabLayoutMediator?.attach()
    }

    private companion object {
        const val FAVORITE_LIST = ""
        const val PLAYLIST = ""
    }
}
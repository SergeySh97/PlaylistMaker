package com.google.playlistmaker.media.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.playlistmaker.databinding.FragmentFavoriteBinding
import com.google.playlistmaker.media.ui.viewmodel.FavoriteVM
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteFragment: Fragment() {

    private val viewModel: FavoriteVM by viewModel {
        val favoriteList = requireArguments().getString(FAVORITE_LIST) ?: ""
        parametersOf(favoriteList)
    }

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeFavoriteList().observe(viewLifecycleOwner) {
            showFavoriteList(it)
        }
    }

    private fun showFavoriteList(favoriteList: String) {
        if (favoriteList.isEmpty()) {
            binding.llError.visible()
        }
    }

    companion object {
        private const val FAVORITE_LIST = "favorite_list"

        fun newInstance(favoriteList: String) = FavoriteFragment().apply {
            arguments = Bundle().apply {
                putString(FAVORITE_LIST, favoriteList)
            }
        }
    }
}
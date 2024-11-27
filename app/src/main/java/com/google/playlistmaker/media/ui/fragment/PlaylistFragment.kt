package com.google.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.playlistmaker.databinding.FragmentPlaylistBinding
import com.google.playlistmaker.media.ui.viewmodel.PlaylistVM
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistVM by viewModel {
        val playlist = requireArguments().getString(PLAYLIST) ?: ""
        parametersOf(playlist)
    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding get() = requireNotNull(_binding) { "Binding wasn't initialized!" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            showFavoriteList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showFavoriteList(playlist: String) {
        if (playlist.isEmpty()) {
            binding.llError.visible()
        }
    }

    companion object {
        private const val PLAYLIST = "playlist"

        fun newInstance(playlist: String) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putString(PLAYLIST, playlist)
            }
        }
    }
}
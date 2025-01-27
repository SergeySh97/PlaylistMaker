package com.google.playlistmaker.media.playlists.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.playlistmaker.databinding.FragmentPlaylistBinding
import com.google.playlistmaker.media.playlists.domain.model.Playlist
import com.google.playlistmaker.media.playlists.ui.OnPlaylistClickListener
import com.google.playlistmaker.media.playlists.ui.PlaylistAdapter
import com.google.playlistmaker.media.playlists.ui.model.PlaylistsState
import com.google.playlistmaker.media.playlists.ui.viewmodel.PlaylistVM
import com.google.playlistmaker.media.ui.fragment.MediaFragmentDirections
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(), OnPlaylistClickListener {

    private val listener: OnPlaylistClickListener by lazy {
        this
    }

    private val viewModel: PlaylistVM by viewModel()

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
        initializeUI()
    }

    private fun initializeUI() {

        viewModel.getPlaylistsState().observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding.btNewPlaylist.setOnClickListener {
            findNavController().navigate(MediaFragmentDirections.actionMediaFragmentToCreatorFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Empty -> showEmpty()
            is PlaylistsState.Content -> showContent(state.playlistList)
        }
    }

    private fun showEmpty() {
        binding.llError.visible()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(list: List<Playlist>) {
        with(binding) {
            val layoutManager = GridLayoutManager(requireContext(), 2)
            rvPlaylist.layoutManager = layoutManager
            val adapter = PlaylistAdapter(list, listener)
            llError.gone()
            rvPlaylist.visible()
            rvPlaylist.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    override fun onPlaylistClick(playlist: Playlist) {
        TODO()
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}
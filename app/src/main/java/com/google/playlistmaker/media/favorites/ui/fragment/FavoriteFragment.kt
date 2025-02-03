package com.google.playlistmaker.media.favorites.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.playlistmaker.databinding.FragmentFavoriteBinding
import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.media.favorites.ui.model.FavoriteState
import com.google.playlistmaker.media.favorites.ui.viewmodel.FavoriteVM
import com.google.playlistmaker.media.media.mapper.Mapper.toTrack
import com.google.playlistmaker.media.media.ui.MediaTrackAdapter
import com.google.playlistmaker.media.media.ui.OnMediaTrackClickListener
import com.google.playlistmaker.media.media.ui.fragment.MediaFragmentDirections
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(), OnMediaTrackClickListener {

    private val viewModel: FavoriteVM by viewModel()

    private val listener: OnMediaTrackClickListener by lazy {
        this
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding get() = requireNotNull(_binding) { "Binding wasn't initialized!" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoritesState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onTrackClick(mediaTrack: MediaTrack) {
        findNavController().navigate(
            MediaFragmentDirections.actionMediaFragmentToPlayerFragment(mediaTrack.toTrack())
        )
    }

    private fun renderState(state: FavoriteState) {
        when (state) {
            is FavoriteState.HaveTracks -> showFavoriteList(state.trackList)
            is FavoriteState.NoTracks -> showEmpty()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFavoriteList(favoriteList: List<MediaTrack>) {
        with(binding) {
            if (favoriteList.isEmpty()) {
                binding.llError.visible()
            } else {
                val adapter = MediaTrackAdapter(favoriteList, listener)
                val recyclerView = rvFavorites
                llError.gone()
                rvFavorites.visible()
                recyclerView.visible()
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun showEmpty() {
        with(binding) {
            rvFavorites.gone()
            llError.visible()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
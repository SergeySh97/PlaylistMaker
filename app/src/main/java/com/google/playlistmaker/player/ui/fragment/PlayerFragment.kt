package com.google.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.FragmentPlayerBinding
import com.google.playlistmaker.media.playlists.domain.model.Playlist
import com.google.playlistmaker.media.playlists.ui.OnPlaylistClickListener
import com.google.playlistmaker.media.playlists.ui.PlaylistBottomSheetAdapter
import com.google.playlistmaker.player.ui.model.PlayerState
import com.google.playlistmaker.player.ui.viewmodel.PlayerVM
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment(), OnPlaylistClickListener {

    private val listener: OnPlaylistClickListener by lazy {
        this
    }

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = requireNotNull(_binding) { "Binding wasn't initialized" }

    private val args by navArgs<PlayerFragmentArgs>()
    private val track by lazy { args.track }
    private val viewModel: PlayerVM by viewModel()
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var isTrackInPlaylist: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {

        observers()
        views()
        clickListeners()
        bottomSheet()
    }

    private fun observers() {

        viewModel.getPlayerState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.getPlayingState().observe(viewLifecycleOwner) {
            playbackControl(it)
        }
        viewModel.getTimerState().observe(viewLifecycleOwner) {
            binding.tvTrackTime.text = dateFormat.format(it)
        }
        viewModel.getFavoriteState().observe(viewLifecycleOwner) {
            favoriteState(it)
        }
        viewModel.observePlaylists().observe(viewLifecycleOwner) {
            showPlaylistList(it)
        }
        viewModel.getIsTrackInPlaylist().observe(viewLifecycleOwner) {
            isTrackInPlaylist = it
        }
    }

    private fun views() {
        track.let { t ->
            viewModel.checkFavorites(t.trackId)
            with(binding) {
                tvTrackName.text = t.trackName
                tvArtistName.text = t.artistName.trim()
                tvDuration.text = dateFormat.format(t.trackTimeMillis.toLong())
                tvYear.text = if (t.releaseDate == NONE) t.releaseDate else t
                    .releaseDate.take(4)
                tvGenre.text = t.primaryGenreName
                tvCountry.text = t.country
                @SuppressLint("SetTextI18n")
                tvTrackTime.text = DEFAULT_TIME
                if (t.collectionName == NULL) {
                    llAlbum.gone()
                } else {
                    llAlbum.visible()
                    tvAlbum.text = t.collectionName
                }
                val radiusInDp = 8
                val density = resources.displayMetrics.density
                val radiusInPixels = (radiusInDp * density).toInt()
                Glide.with(requireContext())
                    .load(t.getCoverArtwork())
                    .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                    .placeholder(R.drawable.placeholder)
                    .into(ivAlbum)


            }
        }
    }

    private fun clickListeners() {
        with(binding) {
            btBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            btLike.setOnClickListener {
                viewModel.onFavoriteClicked(track)
            }
        }
    }

    private fun bottomSheet() {
        with(binding) {
            val bottomSheetBehavior = BottomSheetBehavior.from(bsAddToPlaylist).apply {
                state = if (viewModel.getIsBottomSheetCollapsed().value!!) {
                    BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    BottomSheetBehavior.STATE_HIDDEN
                }
            }
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            overlay.visibility = View.GONE
                        }

                        else -> {
                            overlay.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val alpha = (slideOffset + 1) / 2
                    overlay.alpha = alpha
                }
            })
            btAddInPlaylist.setOnClickListener {
                viewModel.updatePlaylistsList()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            btNewPlaylistBottomSheet.setOnClickListener {
                viewModel.releasePlayer()
                viewModel.updateBottomSheetState()
                findNavController()
                    .navigate(PlayerFragmentDirections.actionPlayerFragmentToCreatorFragment())
            }
        }
    }

    private fun renderState(state: PlayerState) {
        when (state) {
            is PlayerState.Prepared -> preparePlayer()
            is PlayerState.Playing -> startPlayer()
            is PlayerState.Paused -> pausePlayer()
        }
    }

    private fun playbackControl(isPlaying: Boolean) {
        with(binding) {
            btPlay.setImageResource(
                if (isPlaying) {
                    R.drawable.bt_pause
                } else {
                    R.drawable.bt_play
                }
            )
            btPlay.setOnClickListener {
                if (isPlaying) {
                    viewModel.pausePlayer()
                } else {
                    viewModel.startPlayer()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun preparePlayer() {
        with(binding) {
            if (track.previewUrl != NULL) {
                viewModel.preparePlayer(track.previewUrl,
                    {
                        onPreparedListener()
                    }, {
                        onCompletionListener()
                    })
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.null_preview_url,
                    Toast.LENGTH_LONG).show()
                btPlay.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        R.string.null_preview_url,
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun onPreparedListener() {
        binding.btPlay.isEnabled = true
    }

    private fun onCompletionListener() {
        with(binding) {
            btPlay.setImageResource(R.drawable.bt_play)
            tvTrackTime.text = DEFAULT_TIME
            viewModel.pausePlayer()
        }
    }

    private fun startPlayer() {
        viewModel.startPlayer()
    }

    private fun pausePlayer() {
        viewModel.pausePlayer()
    }

    private fun favoriteState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btLike.setImageResource(R.drawable.bt_like_full)
        } else {
            binding.btLike.setImageResource(R.drawable.bt_like_empty)
        }
        track.isFavorite = isFavorite
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPlaylistList(playlistsList: List<Playlist>) {
        val adapter = PlaylistBottomSheetAdapter(
            playlistsList,
            listener
        )
        binding.rvPlaylistBottomSheet.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onPlaylistClick(playlist: Playlist) {
        viewModel.addTrackInPlaylist(track, playlist)
        addTrackIntoPlaylist(isTrackInPlaylist, playlist.name)
    }

    private fun addTrackIntoPlaylist(isTrackInPlaylist: Boolean?, playlistName: String) {
        if (isTrackInPlaylist == true) {
            Toast.makeText(
                requireContext(),
                "Трек уже добавлен в плейлист $playlistName",
                Toast.LENGTH_SHORT).show()
        } else {
            BottomSheetBehavior.from(binding.bsAddToPlaylist).state =
                BottomSheetBehavior.STATE_HIDDEN
            Toast.makeText(
                requireContext(),
                "Добавлено в плейлист $playlistName",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.getPlayingState().value == true) {
            viewModel.pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
        _binding = null
    }

    private companion object {
        const val NULL = "null"
        const val NONE = "Неизвестно"
        const val DEFAULT_TIME = "00:00"
    }
}
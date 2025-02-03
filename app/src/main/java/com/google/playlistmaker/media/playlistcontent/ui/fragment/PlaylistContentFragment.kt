package com.google.playlistmaker.media.playlistcontent.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.FragmentPlaylistContentBinding
import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.media.media.mapper.Mapper.toTrack
import com.google.playlistmaker.media.playlistcontent.ui.OnPlaylistTrackClickListener
import com.google.playlistmaker.media.playlistcontent.ui.PlaylistContentAdapter
import com.google.playlistmaker.media.playlistcontent.ui.model.PlaylistContentState
import com.google.playlistmaker.media.playlistcontent.ui.viewmodel.PlaylistContentVM
import com.google.playlistmaker.media.media.domain.model.Playlist
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.setTimeText
import com.google.playlistmaker.utils.Extensions.setTrackText
import com.google.playlistmaker.utils.Extensions.showLongToast
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistContentFragment : Fragment(), OnPlaylistTrackClickListener {

    private val viewModel: PlaylistContentVM by viewModel()

    private val args by navArgs<PlaylistContentFragmentArgs>()
    private var _playlist: Playlist? = null
    private val playlist: Playlist get() = requireNotNull(_playlist) { "Playlist wasn't initialized!" }

    private var _binding: FragmentPlaylistContentBinding? = null
    private val binding: FragmentPlaylistContentBinding get() = requireNotNull(_binding) { "Binding wasn't initialized!" }

    private var _adapter: PlaylistContentAdapter? = null
    private val adapter: PlaylistContentAdapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized!" }

    private val dateFormatMinutes by lazy { SimpleDateFormat("mm", Locale.getDefault()) }
    private val dateFormatMinutesAndSeconds by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val listener : OnPlaylistTrackClickListener by lazy {
        this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {

        _playlist = args.playlist

        viewModel.observeTrackListLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }

        with(binding) {
            playlist.let { p->
                val trackList = jsonToTrackList(p.trackList)
                val sumTime = trackList.sumOf { it.trackTimeMillis.toLong() }
                tvPlaylistName.text = p.name
                if (p.description.isEmpty()) {
                    tvPlaylistDescription.gone()
                } else {
                    tvPlaylistDescription.text = p.description
                }
                tvPlaylistTime.setTimeText(dateFormatMinutes.format(sumTime).toInt())
                tvPlaylistTracks.setTrackText(p.tracksCount)
                setImage(ivPlaylistArt)
            }
            btBackContent.setOnClickListener {
                findNavController().navigateUp()
            }
            ivShare.setOnClickListener {
                share()
            }
            val bottomSheetBehavior = BottomSheetBehavior.from(bsPlaylistEditor).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            ivEdit.setOnClickListener {
                setImage(ivCoverBottomSheetEditor)
                tvPlaylistNameBottomSheetEditor.text = playlist.name
                tvTracksCountBottomSheetEditor.setTrackText(playlist.tracksCount)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
            tvShareBottomSheet.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                share()
            }
            tvEditInfo.setOnClickListener {
                findNavController()
                    .navigate(PlaylistContentFragmentDirections
                        .actionPlaylistContentFragmentToEditorFragment(playlist))
            }
            tvDeletePlaylist.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                wannaDeletePlaylist()
            }
        }
    }

    private fun renderState(state: PlaylistContentState) {

        when(state) {
            is PlaylistContentState.Launch -> launchTrackList()
            is PlaylistContentState.Content -> updateViews(state.playlist)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun launchTrackList() {
        with(binding) {
            if (playlist.tracksCount > 0) {
                binding.tvNoTracks.gone()
                _adapter = PlaylistContentAdapter(jsonToTrackList(playlist.trackList), listener)
                rvPlaylistContent.adapter = adapter
                adapter.notifyDataSetChanged()
            } else {
                tvNoTracks.visible()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateViews(updatePlaylist: Playlist) {
        _playlist = updatePlaylist
        val trackList = jsonToTrackList(playlist.trackList)
        val sumTime = trackList.sumOf { it.trackTimeMillis.toLong() }
        with(binding) {
            _adapter = PlaylistContentAdapter(trackList, listener)
            rvPlaylistContent.adapter = adapter
            adapter.notifyDataSetChanged()
            tvPlaylistTime.setTimeText(dateFormatMinutes.format(sumTime).toInt())
            tvPlaylistTracks.setTrackText(playlist.tracksCount)
            if (playlist.tracksCount > 0) {
                tvNoTracks.gone()
            } else {
                tvNoTracks.visible()
            }

        }
    }

    private fun jsonToTrackList(json: String?): List<MediaTrack> {
        val listType = object : TypeToken<List<MediaTrack>>() {}.type
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson<List<MediaTrack>?>(json, listType).reversed()
        }
    }

    private fun listForShare(): String {
        val trackList = jsonToTrackList(playlist.trackList)
        val stringBuilder = StringBuilder()
        stringBuilder.append("Название плейлиста: ${playlist.name}\n")
        stringBuilder.append("Описание плейлиста: ${playlist.description}\n")
        stringBuilder.append("Количество треков: ${playlist.tracksCount}\n")
        stringBuilder.append("Список треков:\n")
        trackList.forEachIndexed { index, track ->
            stringBuilder.append("${index + 1}. ${track.artistName} - ${track.trackName} " +
                    "(${dateFormatMinutesAndSeconds.format(track.trackTimeMillis.toLong())})\n"
            )
        }
        return stringBuilder.toString()
    }

    private fun wannaDeletePlaylist() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(R.string.delete_playlist)
            .setMessage("Вы уверены, что хотите удалить плейлист \"${playlist.name}?\"")
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }.setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deletePlaylist(playlist)
                requireContext().showLongToast("Плейлист ${playlist.name} удален")
                findNavController().navigateUp()
            }.show()
    }

    override fun onPlaylistTrackClick(track: MediaTrack) {
        findNavController().navigate(
            PlaylistContentFragmentDirections
                .actionPlaylistContentFragmentToPlayerFragment(track.toTrack())
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPlaylistTrackLongClick(track: MediaTrack) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(R.string.delete_track)
            .setMessage(R.string.wanna_delete_track)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteTrack(track, playlist)
            }
            .show()
    }

    private fun setImage(view: ImageView) {
        Glide.with(requireContext())
            .load(playlist.filePath)
            .transform(CenterCrop())
            .placeholder(R.drawable.placeholder_312)
            .into(view)
    }

    private fun share() {
        if (playlist.tracksCount > 0) {
            viewModel.sharePlaylist(requireContext(), listForShare())
        } else {
            requireActivity().showLongToast(getString(R.string.no_tracks_for_share))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _adapter = null
        _playlist = null
    }
}
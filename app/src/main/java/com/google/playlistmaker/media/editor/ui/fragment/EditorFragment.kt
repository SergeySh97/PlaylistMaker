package com.google.playlistmaker.media.editor.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.playlistmaker.R
import com.google.playlistmaker.media.creator.ui.fragment.CreatorFragment
import com.google.playlistmaker.media.editor.ui.viewmodel.EditorVM
import com.google.playlistmaker.media.media.domain.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditorFragment : CreatorFragment() {

    private val viewModel: EditorVM by viewModel()

    private val args by navArgs<EditorFragmentArgs>()

    private val playlist by lazy {
        args.playlist
    }

    private val callback: OnBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                    findNavController().navigateUp()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        with(binding) {
            tvTitle.text = getString(R.string.edit)
            btCreate.text = getString(R.string.save)
            btCreate.setOnClickListener {
                if (playlistFilePath.isEmpty()) {
                    playlistFilePath = playlist.filePath
                }
                val updatePlaylist = Playlist(
                    playlistId = playlist.playlistId,
                    name = playlistName,
                    description = playlistDescription,
                    filePath = playlistFilePath,
                    trackList = playlist.trackList,
                    tracksCount = playlist.tracksCount
                )
                viewModel.createPlaylist(updatePlaylist)
                val action = EditorFragmentDirections
                    .actionEditorFragmentToPlaylistContentFragment(updatePlaylist)
                findNavController()
                    .navigate(
                        action.actionId,
                        action.arguments,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.editorFragment, true)
                            .setLaunchSingleTop(true)
                            .build()
                    )
            }
            etPlaylistName.text = Editable.Factory.getInstance().newEditable(playlist.name)
            etPlaylistDescription.text =
                Editable.Factory.getInstance().newEditable(playlist.description)

            Glide.with(requireContext())
                .load(playlist.filePath)
                .transform(CenterCrop())
                .placeholder(R.drawable.bt_add_photo)
                .into(ivCover)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }
}
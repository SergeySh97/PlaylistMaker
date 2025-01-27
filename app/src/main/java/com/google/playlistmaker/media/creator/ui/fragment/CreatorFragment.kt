package com.google.playlistmaker.media.creator.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.FragmentCreatorBinding
import com.google.playlistmaker.media.creator.ui.viewmodel.CreatorVM
import com.google.playlistmaker.media.playlists.domain.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatorFragment : Fragment() {

    private val viewModel: CreatorVM by viewModel()

    private var _binding: FragmentCreatorBinding? = null
    private val binding: FragmentCreatorBinding get() = requireNotNull(_binding) { "Binding wasn't initialized!" }

    private var playlistName = ""
    private var playlistDescription = ""
    private var playlistFilePath = ""
    private val callback: OnBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (
                    playlistName != "" ||
                    playlistDescription != "" ||
                    playlistFilePath != ""
                ) {
                    showAlertDialog()
                } else {
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeUI()
    }

    private fun initializeUI() {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    saveImageToPrivateStorage(uri)
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(binding.ivCover)
                }
            }

        with(binding) {

            btBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            ivCover.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            btCreate.setOnClickListener {
                viewModel.createPlaylist(
                    Playlist(
                        playlistId = 0,
                        name = playlistName,
                        description = playlistDescription,
                        filePath = playlistFilePath,
                        trackList = "",
                        tracksCount = 0
                    )
                )
                Toast.makeText(requireContext(), "Плейлист $playlistName создан", Toast.LENGTH_LONG)
                    .show()
                findNavController().navigateUp()
            }
            @Suppress("DEPRECATION")
            etPlaylistName.addTextChangedListener(
                onTextChanged = { s, _, _, _ ->
                    if (s?.isBlank() == true) {
                        btCreate.isEnabled = false
                        btCreate.setBackgroundColor(resources.getColor(R.color.grey))
                    } else {
                        playlistName = s.toString()
                        btCreate.isEnabled = true
                        btCreate.setBackgroundColor(resources.getColor(R.color.blue))
                    }
                }
            )
            etPlaylistDescription.addTextChangedListener(
                onTextChanged = { s, _, _, _ ->
                    playlistDescription = s.toString()
                }
            )
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "IMG_${System.currentTimeMillis()}.jpg")
        playlistFilePath = file.absolutePath
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.finish_creating)
            .setMessage(R.string.data_will_be_lost)
            .setNeutralButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(R.string.complete) { _, _ ->
                findNavController().navigateUp()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        callback.remove()
    }
}
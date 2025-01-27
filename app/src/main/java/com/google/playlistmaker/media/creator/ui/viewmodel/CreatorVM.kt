package com.google.playlistmaker.media.creator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.playlists.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatorVM(
    private val creatorInteractor: CreatorInteractor
) : ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                creatorInteractor.createPlaylist(playlist)
            }
        }
    }
}
package com.google.playlistmaker.media.creator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.media.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CreatorVM(
    private val creatorInteractor: CreatorInteractor
) : ViewModel() {

    open fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                creatorInteractor.createPlaylist(playlist)
            }
        }
    }
}
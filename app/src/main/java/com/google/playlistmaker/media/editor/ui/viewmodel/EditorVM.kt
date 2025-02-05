package com.google.playlistmaker.media.editor.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.creator.ui.viewmodel.CreatorVM
import com.google.playlistmaker.media.media.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorVM(
    private val creatorInteractor: CreatorInteractor
): CreatorVM(creatorInteractor = creatorInteractor) {

    override fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                creatorInteractor.updatePlaylist(playlist)
            }
        }
    }
}
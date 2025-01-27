package com.google.playlistmaker.media.playlists.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.playlists.ui.model.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistVM(
    private val creatorInteractor: CreatorInteractor
) : ViewModel() {

    private val playlistsState = MutableLiveData<PlaylistsState>()

    init {
        viewModelScope.launch {
            creatorInteractor.getPlaylists().collect { playlistList ->
                if (playlistList.isEmpty()) {
                    playlistsState.value = PlaylistsState.Empty
                } else {
                    playlistsState.value = PlaylistsState.Content(playlistList)
                }
            }
        }
    }

    fun getPlaylistsState(): LiveData<PlaylistsState> = playlistsState
}
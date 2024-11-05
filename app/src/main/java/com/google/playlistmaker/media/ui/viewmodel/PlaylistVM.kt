package com.google.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistVM(
    playlist: String
): ViewModel() {

    private val playlistLiveData = MutableLiveData(playlist)

    fun observePlaylist(): LiveData<String> {
        return playlistLiveData
    }
}
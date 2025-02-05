package com.google.playlistmaker.media.playlistcontent.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.media.playlistcontent.ui.model.PlaylistContentState
import com.google.playlistmaker.media.media.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistContentVM(private val creatorInteractor: CreatorInteractor) : ViewModel() {

    private val trackListLiveData = MutableLiveData<PlaylistContentState>()

    init {
        trackListLiveData.value = PlaylistContentState.Launch
    }

    fun observeTrackListLiveData(): LiveData<PlaylistContentState> = trackListLiveData

    fun deleteTrack(track: MediaTrack, playlist: Playlist) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                creatorInteractor.deleteTrackFromPlaylist(track, playlist).collect {
                    withContext(Dispatchers.Main) {
                        trackListLiveData.value = PlaylistContentState.Content(it)
                    }
                }
            }
        }
    }
    fun sharePlaylist(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            creatorInteractor.deletePlaylist(playlist)
        }
    }
}
package com.google.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.favorites.domain.usecase.FavoritesInteractor
import com.google.playlistmaker.media.playlists.domain.model.Playlist
import com.google.playlistmaker.player.domain.usecase.PlayerInteractor
import com.google.playlistmaker.player.ui.model.PlayerState
import com.google.playlistmaker.search.data.mapper.Mapper.toMediaTrack
import com.google.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerVM(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val creatorInteractor: CreatorInteractor
) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>()
    private val playingState = MutableLiveData<Boolean>()
    private val timerState = MutableLiveData<Long>()
    private val favoriteState = MutableLiveData<Boolean>()
    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    private val isTrackInPlayListLiveData = MutableLiveData<Boolean>()
    private val isBottomSheetCollapsed = MutableLiveData(false)
    private var timerJob: Job? = null

    init {
        renderState(PlayerState.Prepared)
        playingState.value = playerInteractor.isPlaying()
    }

    fun getPlayerState(): LiveData<PlayerState> = playerState

    fun getPlayingState(): LiveData<Boolean> = playingState

    fun getTimerState(): LiveData<Long> = timerState

    fun getFavoriteState(): LiveData<Boolean> = favoriteState

    fun observePlaylists(): LiveData<List<Playlist>> = playlistsLiveData

    fun getIsBottomSheetCollapsed(): LiveData<Boolean> = isBottomSheetCollapsed

    fun getIsTrackInPlaylist(): LiveData<Boolean> = isTrackInPlayListLiveData

    fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        playerInteractor.prepare(url, onPreparedListener, onCompletionListener)
    }

    fun startPlayer() {
        startTimer()
        playerInteractor.start()
        playingState.value = playerInteractor.isPlaying()
        renderState(PlayerState.Playing)
    }

    fun pausePlayer() {
        timerJob?.cancel()
        playerInteractor.pause()
        playingState.value = playerInteractor.isPlaying()
        renderState(PlayerState.Paused)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            delay(TRACK_TIME_DELAY)
            timerState.postValue(playerInteractor.getCurrentPosition())
        }
    }

    fun releasePlayer() {
        playerInteractor.release()
    }

    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (track.isFavorite) {
                    favoritesInteractor.addToFavorites(track.toMediaTrack(System.currentTimeMillis()))
                } else {
                    favoritesInteractor.deleteFromFavorites(track.toMediaTrack(0))
                }
            }
        }
        track.isFavorite = !track.isFavorite
        favoriteState.postValue(track.isFavorite)
    }

    fun checkFavorites(trackId: Int) {
        viewModelScope.launch {
            val favoritesList = favoritesInteractor.getFavoritesId()
            favoriteState.postValue(favoritesList.contains(trackId))
        }
    }

    fun updatePlaylistsList() {
        viewModelScope.launch {
            creatorInteractor.getPlaylists().collect {
                playlistsLiveData.value = it
            }
        }
    }

    fun updateBottomSheetState() {
        isBottomSheetCollapsed.value = isBottomSheetCollapsed.value == false
    }

    fun addTrackInPlaylist(track: Track, playlist: Playlist) {
        val trackList = jsonToTrackList(playlist.trackList)
        if (trackList.contains(track)) {
            isTrackInPlayListLiveData.value = true
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    creatorInteractor.addTrackIntoPlaylist(
                        track.toMediaTrack(System.currentTimeMillis()),
                        playlist
                    )
                }
            }
            isTrackInPlayListLiveData.value = false
        }
    }

    private fun jsonToTrackList(json: String?): List<Track> {
        val listType = object : TypeToken<List<Track>>() {}.type
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(json, listType) ?: emptyList()
        }
    }

    private companion object {
        const val TRACK_TIME_DELAY = 300L
    }
}
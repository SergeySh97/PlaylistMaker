package com.google.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.playlistmaker.player.domain.usecase.PlayerInteractor
import com.google.playlistmaker.player.ui.model.PlayerState

class PlayerVM(
    private val trackInteractor: PlayerInteractor
) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>()
    private val playingState = MutableLiveData<Boolean>()

    init {
        renderState(PlayerState.Prepared)
        playingState.value = false
    }

    fun getPlayerState(): LiveData<PlayerState> = playerState
    fun getPlayingState(): LiveData<Boolean> = playingState

    fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        trackInteractor.prepare(url, onPreparedListener, onCompletionListener)
    }

    fun startPlayer() {
        playingState.value = true
        trackInteractor.start()
        renderState(PlayerState.Playing)
    }

    fun pausePlayer() {
        playingState.value = false
        trackInteractor.pause()
        renderState(PlayerState.Paused)
    }

    fun getCurrentPosition(): Long {
        return trackInteractor.getCurrentPosition()
    }

    fun releasePlayer() {
        trackInteractor.release()
    }

    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    override fun onCleared() {
        trackInteractor.release()
    }

}
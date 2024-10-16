package com.google.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.playlistmaker.player.domain.usecase.PlayerInteractor
import com.google.playlistmaker.player.ui.model.PlayerState

class PlayerVM(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>()
    private val playingState = MutableLiveData<Boolean>()

    init {
        renderState(PlayerState.Prepared)
        playingState.value = false
    }

    fun getPlayerState(): LiveData<PlayerState> {
        return playerState
    }

    fun getPlayingState(): LiveData<Boolean> {
        return playingState
    }

    fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        playerInteractor.prepare(url, onPreparedListener, onCompletionListener)
    }

    fun startPlayer() {
        playingState.value = true
        playerInteractor.start()
        renderState(PlayerState.Playing)
    }

    fun pausePlayer() {
        playingState.value = false
        playerInteractor.pause()
        renderState(PlayerState.Paused)
    }

    fun getCurrentPosition(): Long {
        return playerInteractor.getCurrentPosition()
    }

    fun releasePlayer() {
        playerInteractor.release()
    }

    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    override fun onCleared() {
        playerInteractor.release()
    }

}
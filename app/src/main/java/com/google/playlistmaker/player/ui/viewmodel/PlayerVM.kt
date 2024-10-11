package com.google.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.playlistmaker.app.Creator
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

    fun getPlayerState(): LiveData<PlayerState> = playerState
    fun getPlayingState(): LiveData<Boolean> = playingState

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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val playerInteractor = Creator.providerPlayerInteractor()
                PlayerVM(playerInteractor)
            }
        }
    }//rm

}
package com.google.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.player.domain.usecase.PlayerInteractor
import com.google.playlistmaker.player.ui.model.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerVM(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>()
    private val playingState = MutableLiveData<Boolean>()
    private val timerState = MutableLiveData<Long>()
    private var timerJob: Job? = null

    init {
        renderState(PlayerState.Prepared)
        playingState.value = playerInteractor.isPlaying()
    }

    fun getPlayerState(): LiveData<PlayerState> {
        return playerState
    }

    fun getPlayingState(): LiveData<Boolean> {
        return playingState
    }

    fun getTimerState(): LiveData<Long> {
        return timerState
    }

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

    private companion object {
        const val TRACK_TIME_DELAY = 300L
    }
}
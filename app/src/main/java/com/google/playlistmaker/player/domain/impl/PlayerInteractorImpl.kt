package com.google.playlistmaker.player.domain.impl

import com.google.playlistmaker.player.domain.api.PlayerRepository
import com.google.playlistmaker.player.domain.usecase.PlayerInteractor

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
    override fun prepare(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        repository.prepare(url, onPreparedListener, onCompletionListener)
    }

    override fun start() {
        repository.start()
    }

    override fun pause() {
        repository.pause()
    }

    override fun getCurrentPosition(): Long {
        return repository.getCurrentPosition()
    }

    override fun release() {
        repository.release()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

}
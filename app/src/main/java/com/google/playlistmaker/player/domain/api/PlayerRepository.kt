package com.google.playlistmaker.player.domain.api

interface PlayerRepository {

    fun prepare(url: String,
                onPreparedListener: () -> Unit,
                onCompletionListener: () -> Unit)
    fun start()
    fun pause()
    fun getCurrentPosition(): Long
    fun release()
    fun isPlaying(): Boolean
}
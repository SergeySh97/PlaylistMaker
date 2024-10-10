package com.google.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.google.playlistmaker.player.domain.api.PlayerRepository

class PlayerRepositoryImpl(private val mediaPlayer : MediaPlayer) : PlayerRepository {

    override fun prepare(url: String,
                         onPreparedListener: () -> Unit,
                         onCompletionListener: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPreparedListener()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletionListener()
        }
    }
    override fun start() {
        mediaPlayer.start()
    }

    override fun pause(){
        mediaPlayer.pause()
    }

    override fun getCurrentPosition() : Long {
        return mediaPlayer.currentPosition.toLong()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}
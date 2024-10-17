package com.google.playlistmaker.player.ui.model


interface PlayerState {
    data object Prepared : PlayerState
    data object Playing : PlayerState
    data object Paused : PlayerState
}
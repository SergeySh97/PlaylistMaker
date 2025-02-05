package com.google.playlistmaker.media.favorites.ui.model

import com.google.playlistmaker.media.media.domain.model.MediaTrack


interface FavoriteState {
    data class HaveTracks(val trackList: List<MediaTrack>) : FavoriteState
    data object NoTracks : FavoriteState
}

package com.google.playlistmaker.media.creator.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("track_in_playlist_table")
data class TrackInPlaylistEntity(
    @PrimaryKey
    val trackId: Int,
    val artworkUrl100: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Long,
    val previewUrl: String,
    val timestamp: Long,
    val playlistCount: Int
)
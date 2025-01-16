package com.google.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("track_table")
data class TrackEntity(
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
    val timestamp: Long
)

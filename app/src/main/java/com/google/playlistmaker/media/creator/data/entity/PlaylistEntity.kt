package com.google.playlistmaker.media.creator.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val name: String,
    val description: String,
    val filePath: String,
    val trackList: String,
    val tracksCount: Int
)
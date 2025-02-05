package com.google.playlistmaker.media.media.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int,
    val name: String,
    val description: String,
    val filePath: String,
    val trackList: String,
    val tracksCount: Int
) : Parcelable

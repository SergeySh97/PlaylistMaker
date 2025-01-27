package com.google.playlistmaker.media.favorites.domain.api

import com.google.playlistmaker.media.domain.model.MediaTrack
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addToFavorites(track: MediaTrack)

    suspend fun deleteFromFavorites(track: MediaTrack)

    fun getFavoritesTracks(): Flow<List<MediaTrack>>

    suspend fun getFavoritesId(): List<Int>
}
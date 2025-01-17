package com.google.playlistmaker.favorites.domain.api

import com.google.playlistmaker.favorites.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addToFavorites(track: TrackEntity)

    suspend fun deleteFromFavorites(track: TrackEntity)

    fun getFavoritesTracks(): Flow<List<TrackEntity>>

    suspend fun getFavoritesId(): List<Int>
}
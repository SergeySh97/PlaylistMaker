package com.google.playlistmaker.favorites.domain.usecase

import com.google.playlistmaker.favorites.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    suspend fun addToFavorites(track: TrackEntity)

    suspend fun deleteFromFavorites(track: TrackEntity)

    fun getFavoritesTracks(): Flow<List<TrackEntity>>

    suspend fun getFavoritesId(): List<Int>
}
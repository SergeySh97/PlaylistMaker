package com.google.playlistmaker.db.domain.api

import com.google.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addToFavorites(track: TrackEntity)

    fun deleteFromFavorites(track: TrackEntity)

    fun getFavoritesTracks(): Flow<List<TrackEntity>>

    suspend fun getFavoritesId(): List<Int>
}
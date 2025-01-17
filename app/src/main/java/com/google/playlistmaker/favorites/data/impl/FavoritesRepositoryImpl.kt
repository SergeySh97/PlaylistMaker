package com.google.playlistmaker.favorites.data.impl

import com.google.playlistmaker.favorites.AppDatabase
import com.google.playlistmaker.favorites.data.entity.TrackEntity
import com.google.playlistmaker.favorites.domain.api.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val database: AppDatabase): FavoritesRepository {
    override suspend fun addToFavorites(track: TrackEntity) {
        database.trackDao().insertTracks(track)
    }

    override suspend fun deleteFromFavorites(track: TrackEntity) {
        database.trackDao().deleteTrack(track)
    }

    override fun getFavoritesTracks(): Flow<List<TrackEntity>> {
        return database.trackDao().getTracks().map { entityList -> entityList }
    }

    override suspend fun getFavoritesId(): List<Int> {
        return database.trackDao().getTracksId()
    }
}
package com.google.playlistmaker.media.favorites.data.impl

import com.google.playlistmaker.media.creator.AppDatabase
import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.media.favorites.domain.api.FavoritesRepository
import com.google.playlistmaker.media.media.mapper.Mapper.toMediaTrack
import com.google.playlistmaker.media.media.mapper.Mapper.toTrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val database: AppDatabase) : FavoritesRepository {
    override suspend fun addToFavorites(track: MediaTrack) {
        database.trackDao().insertTrack(track.toTrackEntity())
    }

    override suspend fun deleteFromFavorites(track: MediaTrack) {
        database.trackDao().deleteTrack(track.toTrackEntity())
    }

    override fun getFavoritesTracks(): Flow<List<MediaTrack>> {
        return database.trackDao().getTracks()
            .map { entityList -> entityList.map { trackEntity -> trackEntity.toMediaTrack() } }
    }

    override suspend fun getFavoritesId(): List<Int> {
        return database.trackDao().getTracksId()
    }
}
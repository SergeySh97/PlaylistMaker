package com.google.playlistmaker.search.domain.api

import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.NetworkResult
import com.google.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksRepository {
    fun searchTracks(expression: String): Flow<NetworkResult<List<Track>, ErrorType>>
}
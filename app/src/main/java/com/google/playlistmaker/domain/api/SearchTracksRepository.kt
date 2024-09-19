package com.google.playlistmaker.domain.api

import com.google.playlistmaker.data.network.model.ErrorType
import com.google.playlistmaker.data.network.model.NetworkResult
import com.google.playlistmaker.domain.models.Track

interface SearchTracksRepository {
    fun searchTracks(expression: String): NetworkResult<List<Track>, ErrorType>
}
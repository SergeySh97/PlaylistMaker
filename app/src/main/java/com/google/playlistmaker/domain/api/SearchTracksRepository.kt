package com.google.playlistmaker.domain.api

import com.google.playlistmaker.domain.models.ErrorType
import com.google.playlistmaker.domain.models.NetworkResult
import com.google.playlistmaker.domain.models.Track

interface SearchTracksRepository {
    fun searchTracks(expression: String): NetworkResult<List<Track>, ErrorType>
}
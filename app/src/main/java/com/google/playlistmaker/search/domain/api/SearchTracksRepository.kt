package com.google.playlistmaker.search.domain.api

import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.NetworkResult
import com.google.playlistmaker.search.domain.model.Track

interface SearchTracksRepository {
    fun searchTracks(expression: String): NetworkResult<List<Track>, ErrorType>
}
package com.google.playlistmaker.search.domain.api

import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.NetworkResult
import com.google.playlistmaker.search.domain.model.Track

interface TracksConsumer {
    fun consume(foundTracks: NetworkResult<List<Track>, ErrorType>)
}
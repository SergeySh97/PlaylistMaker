package com.google.playlistmaker.domain.usecases

import com.google.playlistmaker.data.network.model.ErrorType
import com.google.playlistmaker.data.network.model.NetworkResult
import com.google.playlistmaker.domain.models.Track

interface TracksSearchUseCase {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: NetworkResult<List<Track>, ErrorType>)
    }
}
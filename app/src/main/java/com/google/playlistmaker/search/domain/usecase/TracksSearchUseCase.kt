package com.google.playlistmaker.search.domain.usecase

import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.NetworkResult
import com.google.playlistmaker.search.domain.model.Track

interface TracksSearchUseCase {
    fun execute(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: NetworkResult<List<Track>, ErrorType>)
    }
}
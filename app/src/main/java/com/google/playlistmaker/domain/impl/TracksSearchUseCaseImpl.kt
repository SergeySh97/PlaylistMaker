package com.google.playlistmaker.domain.impl

import java.util.concurrent.Executors
import com.google.playlistmaker.domain.usecases.TracksSearchUseCase
import com.google.playlistmaker.domain.api.SearchTracksRepository

class TracksSearchUseCaseImpl(private val respository: SearchTracksRepository):
    TracksSearchUseCase {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TracksSearchUseCase.TracksConsumer) {
        executor.execute {
            consumer.consume(respository.searchTracks(expression))
        }
    }
}
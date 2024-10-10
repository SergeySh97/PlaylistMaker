package com.google.playlistmaker.search.domain.impl

import com.google.playlistmaker.search.domain.api.SearchTracksRepository
import com.google.playlistmaker.search.domain.usecase.TracksSearchUseCase
import java.util.concurrent.Executors

class TracksSearchUseCaseImpl(private val repository: SearchTracksRepository) :
    TracksSearchUseCase {

    private val executor = Executors.newCachedThreadPool()
    override fun execute(expression: String, consumer: TracksSearchUseCase.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}
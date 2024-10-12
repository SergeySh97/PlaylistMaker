package com.google.playlistmaker.search.domain.impl

import com.google.playlistmaker.search.domain.api.SearchTracksRepository
import com.google.playlistmaker.search.domain.api.TracksConsumer
import com.google.playlistmaker.search.domain.usecase.SearchTracksUseCase
import java.util.concurrent.Executors

class SearchTracksUseCaseImpl(private val repository: SearchTracksRepository) :
    SearchTracksUseCase {

    private val executor = Executors.newCachedThreadPool()
    override fun execute(expression: String, consumer: TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}
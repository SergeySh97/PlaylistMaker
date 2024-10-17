package com.google.playlistmaker.search.domain.usecase

import com.google.playlistmaker.search.domain.api.TracksConsumer

interface SearchTracksUseCase {
    fun execute(expression: String, consumer: TracksConsumer)
}
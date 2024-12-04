package com.google.playlistmaker.search.domain.usecase

import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksUseCase {
    fun execute(expression: String): Flow<Pair<List<Track>?, ErrorType?>>
}
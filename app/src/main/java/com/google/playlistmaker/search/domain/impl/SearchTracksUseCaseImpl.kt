package com.google.playlistmaker.search.domain.impl

import com.google.playlistmaker.search.domain.api.SearchTracksRepository
import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.NetworkResult
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.search.domain.usecase.SearchTracksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksUseCaseImpl(private val repository: SearchTracksRepository) :
    SearchTracksUseCase {

    override fun execute(expression: String): Flow<Pair<List<Track>?, ErrorType?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is NetworkResult.Success -> {
                    Pair(result.data, null)
                }

                is NetworkResult.Error -> {
                    Pair(null, result.error)
                }
            }
        }
    }
}
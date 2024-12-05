package com.google.playlistmaker.search.data.impl

import com.google.playlistmaker.search.data.mapper.Mapper.toTrack
import com.google.playlistmaker.search.data.network.NetworkClient
import com.google.playlistmaker.search.data.network.models.StatusCode
import com.google.playlistmaker.search.data.network.models.TracksSearchRequest
import com.google.playlistmaker.search.data.network.models.TracksSearchResponse
import com.google.playlistmaker.search.data.network.models.mapToErrorType
import com.google.playlistmaker.search.domain.api.SearchTracksRepository
import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.NetworkResult
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.search.ui.model.SearchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchTracksRepositoryImpl(private val networkClient: NetworkClient) :
    SearchTracksRepository {

    override fun searchTracks(expression: String): Flow<NetworkResult<List<Track>, ErrorType>> =
        flow {
            val response = networkClient.doRequest(TracksSearchRequest(expression))
            when (response.resultCode) {
                StatusCode(200) -> {
                    val results = (response as TracksSearchResponse).results
                    if (results.isNotEmpty()) {
                        emit(NetworkResult.Success(results.map { it.toTrack() }))
                    } else {
                        emit(NetworkResult.Success(emptyList()))
                    }
                }

                else -> {
                    SearchState.Error(response.resultCode.mapToErrorType())
                }
            }
        }
}
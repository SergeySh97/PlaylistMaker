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


class SearchTracksRepositoryImpl(private val networkClient: NetworkClient) :
    SearchTracksRepository {

    override fun searchTracks(expression: String): NetworkResult<List<Track>, ErrorType> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            StatusCode(200) -> {
                val results = (response as TracksSearchResponse).results
                if (results.isNotEmpty()) {
                    NetworkResult.Success(results.map { it.toTrack() })
                } else {
                    NetworkResult.Success(emptyList())
                }
            }

            else -> {
                NetworkResult.Error(response.resultCode.mapToErrorType())
            }
        }
    }
}
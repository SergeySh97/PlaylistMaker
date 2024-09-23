package com.google.playlistmaker.data.impl

import com.google.playlistmaker.data.converters.Mapper.mapToDomain
import com.google.playlistmaker.data.network.NetworkClient
import com.google.playlistmaker.data.network.models.TracksSearchRequest
import com.google.playlistmaker.data.network.models.TracksSearchResponse
import com.google.playlistmaker.data.network.models.StatusCode
import com.google.playlistmaker.data.network.models.mapToErrorType
import com.google.playlistmaker.domain.models.ErrorType
import com.google.playlistmaker.domain.models.NetworkResult
import com.google.playlistmaker.domain.models.Track
import com.google.playlistmaker.domain.api.SearchTracksRepository


class TracksRepositoryImpl(private val networkClient: NetworkClient) : SearchTracksRepository {

    override fun searchTracks(expression: String): NetworkResult<List<Track>, ErrorType> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            StatusCode(200) -> {
                val results = (response as TracksSearchResponse).results
                if (results.isNotEmpty()) {
                    NetworkResult.Success(results.map { mapToDomain(it) })
                }
                else {
                    NetworkResult.Success(emptyList())
                }
            }
            StatusCode(404) -> {
                NetworkResult.Error(response.resultCode.mapToErrorType())
            }
            else -> {
                NetworkResult.Error(response.resultCode.mapToErrorType())
            }
        }
    }
}
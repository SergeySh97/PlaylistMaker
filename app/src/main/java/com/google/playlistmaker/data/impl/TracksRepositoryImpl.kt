package com.google.playlistmaker.data.impl

import com.google.playlistmaker.data.network.NetworkClient
import com.google.playlistmaker.data.converters.Converters
import com.google.playlistmaker.data.converters.Converters.mapToDomain
import com.google.playlistmaker.data.network.model.TracksSearchRequest
import com.google.playlistmaker.data.network.model.TracksSearchResponse
import com.google.playlistmaker.data.network.model.ErrorType
import com.google.playlistmaker.data.network.model.NetworkResult
import com.google.playlistmaker.data.network.model.StatusCode
import com.google.playlistmaker.data.network.model.mapToErrorType
import com.google.playlistmaker.domain.api.SearchTracksRepository
import com.google.playlistmaker.domain.models.Track


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
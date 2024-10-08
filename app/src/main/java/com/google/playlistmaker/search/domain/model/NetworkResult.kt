package com.google.playlistmaker.search.domain.model


sealed class NetworkResult<T, E> {

    data class Success<T, E>(val data: T) : NetworkResult<T, E>()

    data class Error<T, E>(val error: E) : NetworkResult<T, E>()

}
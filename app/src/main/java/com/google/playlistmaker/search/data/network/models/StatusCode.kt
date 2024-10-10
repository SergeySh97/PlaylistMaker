package com.google.playlistmaker.search.data.network.models

import com.google.playlistmaker.search.domain.model.ErrorType

@JvmInline
value class StatusCode(val code: Int)

fun StatusCode.mapToErrorType(): ErrorType {
    return when (code) {
        -1 -> ErrorType.NO_CONNECTION
        403 -> ErrorType.BAD_REQUEST
        404 -> ErrorType.NOT_FOUND
        501 -> ErrorType.SERVER_ERROR
        else -> ErrorType.UNKNOWN_ERROR
    }
}


package com.google.playlistmaker.data.network.model

@JvmInline
value class StatusCode(val code: Int)

fun StatusCode.mapToErrorType(): ErrorType {
    return when (code) {
        - 1-> ErrorType.NO_CONNECTION
        403 -> ErrorType.BAD_REQUEST
        404 -> ErrorType.NOT_FOUND
        501 -> ErrorType.SERVER_ERROR
        else -> ErrorType.UNKNOWN_ERROR
    }
}


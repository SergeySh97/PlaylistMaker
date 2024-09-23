package com.google.playlistmaker.data.network

import com.google.playlistmaker.data.network.models.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}
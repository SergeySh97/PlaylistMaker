package com.google.playlistmaker.search.data.network

import com.google.playlistmaker.search.data.network.models.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}
package com.google.playlistmaker.search.data.network

import com.google.playlistmaker.search.data.network.models.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")

    fun searchTracks(@Query("term") text: String): Call<TracksSearchResponse>

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}
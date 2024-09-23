package com.google.playlistmaker.data.network

import com.google.playlistmaker.data.network.models.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")

    fun searchTracks(@Query("term") text: String): Call<TracksSearchResponse>
}
package com.google.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.playlistmaker.search.data.network.models.Response
import com.google.playlistmaker.search.data.network.models.StatusCode
import com.google.playlistmaker.search.data.network.models.TracksSearchRequest

class RetrofitNetworkClient(
    private val itunesApi: ItunesApi,
    private val context: Context
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        return if (isConnected(context)) {
            if (dto is TracksSearchRequest) {
                val response = itunesApi.searchTracks(dto.expression).execute()
                val body = response.body() ?: Response()
                if (response.body()?.results?.isNotEmpty() == true) {
                    body.apply { resultCode = StatusCode(response.code()) }
                } else {
                    Response().apply { resultCode = StatusCode(404) }
                }
            } else {
                Response().apply { resultCode = StatusCode(400) }
            }
        } else {
            Response().apply { resultCode = StatusCode(-1) }
        }
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }
}